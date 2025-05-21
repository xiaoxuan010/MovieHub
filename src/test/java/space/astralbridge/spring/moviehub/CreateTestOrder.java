package space.astralbridge.spring.moviehub;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import space.astralbridge.spring.moviehub.entity.PaymentOrder;
import space.astralbridge.spring.moviehub.mapper.PaymentOrderMapper;

/**
 * 用于创建测试订单的辅助类
 * 运行方式：
 * ./mvnw spring-boot:run -Dspring-boot.run.profiles=create-test-order
 */
@SpringBootApplication
@Profile("create-test-order")
public class CreateTestOrder implements CommandLineRunner {

    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    // 固定的测试订单号，用于支付宝异步通知测试
    private static final String TEST_ORDER_NO = "202307120123451234";

    public static void main(String[] args) {
        SpringApplication.run(CreateTestOrder.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 检查订单是否已存在
        PaymentOrder existingOrder = paymentOrderMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<PaymentOrder>()
                        .eq(PaymentOrder::getOrderNo, TEST_ORDER_NO));

        if (existingOrder != null) {
            System.out.println("测试订单已存在，订单号: " + TEST_ORDER_NO);
            return;
        }

        // 创建测试订单
        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(TEST_ORDER_NO);
        order.setUserId(1L); // 假设用户ID为1
        order.setAmount(new BigDecimal("30.00")); // 金额30元
        order.setStatus(0); // 待支付状态
        order.setPayType("alipay");
        order.setVipDuration("monthly");
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        // 插入订单
        int result = paymentOrderMapper.insert(order);

        if (result > 0) {
            System.out.println("测试订单创建成功，订单号: " + TEST_ORDER_NO);
        } else {
            System.out.println("测试订单创建失败");
        }
    }
}
