package space.astralbridge.spring.moviehub.util;

import space.astralbridge.spring.moviehub.entity.PaymentOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 测试订单工具类
 */
public class TestOrderUtil {

    /**
     * 创建测试订单对象
     * @return 测试订单对象
     */
    public static PaymentOrder createTestOrder() {
        PaymentOrder order = new PaymentOrder();
        order.setOrderNo("TEST" + System.currentTimeMillis()); // 生成唯一的订单号
        order.setUserId(1L); // 假设用户ID为1
        order.setAmount(new BigDecimal("30.00")); // 金额30元
        order.setStatus(0); // 待支付状态
        order.setPayType("alipay");
        order.setVipDuration("monthly");
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return order;
    }
    
    /**
     * 创建固定订单号的测试订单对象
     * @param orderNo 指定的订单号
     * @return 测试订单对象
     */
    public static PaymentOrder createTestOrderWithOrderNo(String orderNo) {
        PaymentOrder order = createTestOrder();
        order.setOrderNo(orderNo);
        return order;
    }
}
