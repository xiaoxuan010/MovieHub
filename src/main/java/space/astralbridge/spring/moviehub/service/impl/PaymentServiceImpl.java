package space.astralbridge.spring.moviehub.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.astralbridge.spring.moviehub.config.AlipayConfig;
import space.astralbridge.spring.moviehub.dto.PaymentRequest;
import space.astralbridge.spring.moviehub.entity.PaymentOrder;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.mapper.PaymentOrderMapper;
import space.astralbridge.spring.moviehub.mapper.UserMapper;
import space.astralbridge.spring.moviehub.service.PaymentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl extends ServiceImpl<PaymentOrderMapper, PaymentOrder> implements PaymentService {

    private final AlipayClient alipayClient;
    private final AlipayConfig alipayConfig;
    private final UserMapper userMapper;

    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;

    @Value("${alipay.sign_type}")
    private String signType;

    @Value("${alipay.app_id}")
    private String appId;

    @Override
    @Transactional
    public String createAlipayForm(Long userId, PaymentRequest request) {
        // 验证用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 1. 生成唯一订单号
        String orderNo = generateOrderNo();

        // 验证订单号是否已存在
        if (getOrderByOrderNo(orderNo) != null) {
            // 如果订单号已存在，重新生成
            orderNo = generateOrderNo();
        }

        // 2. 获取VIP价格
        BigDecimal amount = alipayConfig.getVipPrice(request.getDuration());

        // 3. 创建订单记录
        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setAmount(amount);
        order.setStatus(0); // 待支付
        order.setPayType("alipay");
        order.setVipDuration(request.getDuration());

        boolean saved = save(order);
        if (!saved) {
            throw new RuntimeException("订单保存失败");
        }

        log.info("成功创建订单，订单号: {}, 用户ID: {}, 金额: {}", orderNo, userId, amount);

        // 4. 调用支付宝接口生成支付表单
        try {
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());

            // 组装支付请求参数
            String subject = "MovieHub VIP会员-" + ("yearly".equals(request.getDuration()) ? "年度" : "月度");

            // 按照支付宝要求格式化金额为两位小数的字符串
            String amountStr = amount.setScale(2, java.math.RoundingMode.HALF_UP).toString();

            // 构建符合支付宝API要求的JSON格式
            String bizContent = "{" +
                    "\"out_trade_no\":\"" + orderNo + "\"," +
                    "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                    "\"total_amount\":\"" + amountStr + "\"," +
                    "\"subject\":\"" + subject + "\"," +
                    "\"body\":\"" + subject + " 会员购买\"" +
                    "}";

            // 设置bizContent
            alipayRequest.setBizContent(bizContent);

            // 记录支付请求基本信息
            log.info("发起支付宝支付请求，订单号: {}, 金额: {}", orderNo, amountStr);

            // 调用SDK生成表单
            String form = alipayClient.pageExecute(alipayRequest).getBody();
            return form;
        } catch (AlipayApiException e) {
            log.error("生成支付宝支付表单失败: {}", e.getMessage(), e);
            throw new RuntimeException("生成支付宝支付表单失败", e);
        }
    }

    @Override
    public PaymentOrder getOrderByOrderNo(String orderNo) {
        LambdaQueryWrapper<PaymentOrder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PaymentOrder::getOrderNo, orderNo);
        return getOne(wrapper);
    }

    @Override
    public List<PaymentOrder> getOrdersByUserId(Long userId) {
        LambdaQueryWrapper<PaymentOrder> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PaymentOrder::getUserId, userId);
        wrapper.orderByDesc(PaymentOrder::getCreateTime);
        return list(wrapper);
    }

    /**
     * 生成订单号
     * 格式: 时间戳(14位) + 随机数(4位)
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.valueOf((int) (Math.random() * 9000) + 1000);
        return timestamp + random;
    }

    @Override
    public boolean updateById(PaymentOrder order) {
        return super.updateById(order);
    }
}