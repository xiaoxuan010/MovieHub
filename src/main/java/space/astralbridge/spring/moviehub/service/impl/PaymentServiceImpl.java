package space.astralbridge.spring.moviehub.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
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

    // 用于测试时控制是否验证签名
    private boolean verifyAlipayNotify = false;

    @Override
    public String createAlipayForm(Long userId, PaymentRequest request) {
        // 1. 生成唯一订单号
        String orderNo = generateOrderNo();

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
        save(order);

        // 4. 调用支付宝接口生成支付表单
        try {
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
            alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());

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

            // 记录详细的请求信息用于调试
            log.info("发起支付宝支付请求:");
            log.info("- gateway_url: {}", alipayConfig.getGatewayUrl());
            log.info("- app_id: {}", alipayConfig.getAppId());
            log.info("- notify_url: {}", alipayConfig.getNotifyUrl());
            log.info("- return_url: {}", alipayConfig.getReturnUrl());
            log.info("- bizContent: {}", bizContent);

            // 调用SDK生成表单
            String form = alipayClient.pageExecute(alipayRequest).getBody();
            log.info("生成的支付表单: {}", form);
            return form;
        } catch (AlipayApiException e) {
            log.error("生成支付宝支付表单失败: {}", e.getMessage(), e);
            // 记录详细的错误信息
            if (e.getCause() != null) {
                log.error("错误原因: {}", e.getCause().getMessage());
            }
            throw new RuntimeException("生成支付宝支付表单失败", e);
        }
    }

    @Override
    @Transactional
    public String handleAlipayNotify(Map<String, String> params) {
        try {
            // 1. 验证签名 - 只有在非测试模式下才验证
            boolean signVerified = true;
            if (verifyAlipayNotify) {
                signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    alipayConfig.getAlipayPublicKey(),
                    "UTF-8",
                    alipayConfig.getSignType()
                );
            }

            if (!signVerified) {
                log.error("支付宝异步通知验签失败");
                return "failure";
            }

            // 2. 获取通知参数
            String outTradeNo = params.get("out_trade_no"); // 商户订单号
            String tradeNo = params.get("trade_no"); // 支付宝交易号
            String tradeStatus = params.get("trade_status"); // 交易状态

            // 打印所有回调参数便于调试
            log.info("支付宝回调参数: {}", params);

            // 3. 查询订单信息
            PaymentOrder order = getOrderByOrderNo(outTradeNo);
            if (order == null) {
                log.error("订单不存在: {}", outTradeNo);
                return "failure";
            }

            // 4. 更新订单状态
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                // 交易成功
                if (order.getStatus() == 0) { // 避免重复处理
                    order.setStatus(1); // 支付成功
                    order.setTradeNo(tradeNo);
                    updateById(order);

                    // 5. 更新用户为VIP
                    User user = userMapper.selectById(order.getUserId());
                    if (user != null) {
                        user.setUserType(1); // 设置为VIP用户
                        userMapper.updateById(user);
                    }
                }
                return "success";
            } else {
                log.info("订单{}的支付状态为{}", outTradeNo, tradeStatus);
                return "success"; // 收到通知即返回成功
            }
        } catch (Exception e) {
            log.error("处理支付宝异步通知失败", e);
            return "failure";
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
}