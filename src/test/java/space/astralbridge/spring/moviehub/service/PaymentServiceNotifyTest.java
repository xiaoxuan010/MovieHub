package space.astralbridge.spring.moviehub.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import space.astralbridge.spring.moviehub.config.AlipayConfig;
import space.astralbridge.spring.moviehub.entity.PaymentOrder;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.mapper.PaymentOrderMapper;
import space.astralbridge.spring.moviehub.mapper.UserMapper;
import space.astralbridge.spring.moviehub.service.impl.PaymentServiceImpl;
import space.astralbridge.spring.moviehub.util.TestOrderUtil;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 支付宝异步通知处理的详细单元测试
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentServiceNotifyTest {

    @Mock
    private AlipayClient alipayClient;

    @Mock
    private AlipayConfig alipayConfig;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PaymentOrderMapper paymentOrderMapper;

    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        // 创建PaymentServiceImpl实例
        paymentService = spy(new PaymentServiceImpl(alipayClient, alipayConfig, userMapper));

        // 注入依赖
        ReflectionTestUtils.setField(paymentService, "baseMapper", paymentOrderMapper);
        ReflectionTestUtils.setField(paymentService, "alipayPublicKey", "test_public_key");
        ReflectionTestUtils.setField(paymentService, "signType", "RSA2");

        // 配置alipayConfig
        when(alipayConfig.getAlipayPublicKey()).thenReturn("test_public_key");
        when(alipayConfig.getSignType()).thenReturn("RSA2");
    }

    /**
     * 测试处理支付宝异步通知 - 验证签名开启 - 签名验证成功
     */
    @Test
    void testHandleAlipayNotify_WithSignVerification_Success() throws Exception {
        // 1. 准备测试数据
        String orderNo = "TEST_ORDER_" + System.currentTimeMillis();
        PaymentOrder order = TestOrderUtil.createTestOrderWithOrderNo(orderNo);

        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", orderNo);
        params.put("trade_no", "2023071222001425901234567890");
        params.put("trade_status", "TRADE_SUCCESS");
        params.put("total_amount", "30.00");
        params.put("app_id", "2021000148638438");
        params.put("sign", "test_sign");
        params.put("sign_type", "RSA2");

        User user = new User();
        user.setId(1L);
        user.setUserType(0); // 普通用户

        // 2. 配置Mock
        // 启用签名验证
        ReflectionTestUtils.setField(paymentService, "verifyAlipayNotify", true);

        // 使用MockedStatic模拟静态方法
        try (MockedStatic<AlipaySignature> mockedStatic = Mockito.mockStatic(AlipaySignature.class)) {
            // 模拟签名验证成功
            mockedStatic.when(() -> AlipaySignature.rsaCheckV1(anyMap(), anyString(), anyString(), anyString()))
                    .thenReturn(true);

            // 模拟查询订单成功
            doReturn(order).when(paymentService).getOrderByOrderNo(orderNo);

            // 模拟查询用户成功
            when(userMapper.selectById(1L)).thenReturn(user);

            // 3. 执行测试
            String result = paymentService.handleAlipayNotify(params);

            // 4. 验证结果
            assertEquals("success", result);

            // 验证订单状态被更新
            assertEquals(1, order.getStatus());
            assertEquals("2023071222001425901234567890", order.getTradeNo());

            // 验证用户被更新为VIP
            assertEquals(1, user.getUserType());

            // 验证方法调用
            verify(paymentService).getOrderByOrderNo(orderNo);
            verify(userMapper).selectById(1L);
            verify(paymentService).updateById(order);
            verify(userMapper).updateById(user);
        }
    }

    /**
     * 测试处理支付宝异步通知 - 验证签名开启 - 签名验证失败
     */
    @Test
    void testHandleAlipayNotify_WithSignVerification_SignFailed() throws Exception {
        // 1. 准备测试数据
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", "TEST_ORDER_123");
        params.put("sign", "invalid_sign");

        // 2. 配置Mock
        // 启用签名验证
        ReflectionTestUtils.setField(paymentService, "verifyAlipayNotify", true);

        // 使用MockedStatic模拟静态方法
        try (MockedStatic<AlipaySignature> mockedStatic = Mockito.mockStatic(AlipaySignature.class)) {
            // 模拟签名验证失败
            mockedStatic.when(() -> AlipaySignature.rsaCheckV1(anyMap(), anyString(), anyString(), anyString()))
                    .thenReturn(false);

            // 3. 执行测试
            String result = paymentService.handleAlipayNotify(params);

            // 4. 验证结果
            assertEquals("failure", result);

            // 验证没有进行后续处理
            verify(paymentService, never()).getOrderByOrderNo(anyString());
            verify(userMapper, never()).selectById(anyLong());
        }
    }

    /**
     * 测试处理支付宝异步通知 - 验证签名开启 - 签名验证抛出异常
     */
    @Test
    void testHandleAlipayNotify_WithSignVerification_Exception() throws Exception {
        // 1. 准备测试数据
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", "TEST_ORDER_123");
        params.put("sign", "invalid_sign");

        // 2. 配置Mock
        // 启用签名验证
        ReflectionTestUtils.setField(paymentService, "verifyAlipayNotify", true);

        // 使用MockedStatic模拟静态方法
        try (MockedStatic<AlipaySignature> mockedStatic = Mockito.mockStatic(AlipaySignature.class)) {
            // 模拟签名验证抛出异常
            mockedStatic.when(() -> AlipaySignature.rsaCheckV1(anyMap(), anyString(), anyString(), anyString()))
                    .thenThrow(new AlipayApiException("签名验证失败"));

            // 3. 执行测试
            String result = paymentService.handleAlipayNotify(params);

            // 4. 验证结果
            assertEquals("failure", result);

            // 验证没有进行后续处理
            verify(paymentService, never()).getOrderByOrderNo(anyString());
            verify(userMapper, never()).selectById(anyLong());
        }
    }

    /**
     * 测试处理支付宝异步通知 - 验证签名关闭 - 订单不存在
     */
    @Test
    void testHandleAlipayNotify_WithoutSignVerification_OrderNotFound() {
        // 1. 准备测试数据
        String orderNo = "NONEXISTENT_ORDER";
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", orderNo);
        params.put("trade_status", "TRADE_SUCCESS");

        // 2. 配置Mock
        // 关闭签名验证
        ReflectionTestUtils.setField(paymentService, "verifyAlipayNotify", false);

        // 模拟订单不存在
        doReturn(null).when(paymentService).getOrderByOrderNo(orderNo);

        // 3. 执行测试
        String result = paymentService.handleAlipayNotify(params);

        // 4. 验证结果
        assertEquals("failure", result);

        // 验证方法调用
        verify(paymentService).getOrderByOrderNo(orderNo);
        verify(userMapper, never()).selectById(anyLong());
    }

    /**
     * 测试处理支付宝异步通知 - 验证签名关闭 - 交易状态不是成功
     */
    @Test
    void testHandleAlipayNotify_WithoutSignVerification_TradeStatusNotSuccess() {
        // 1. 准备测试数据
        String orderNo = "TEST_ORDER_" + System.currentTimeMillis();
        PaymentOrder order = TestOrderUtil.createTestOrderWithOrderNo(orderNo);

        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", orderNo);
        params.put("trade_status", "TRADE_CLOSED"); // 交易关闭

        // 2. 配置Mock
        // 关闭签名验证
        ReflectionTestUtils.setField(paymentService, "verifyAlipayNotify", false);

        // 模拟查询订单成功
        doReturn(order).when(paymentService).getOrderByOrderNo(orderNo);

        // 3. 执行测试
        String result = paymentService.handleAlipayNotify(params);

        // 4. 验证结果
        assertEquals("success", result); // 即使交易状态不是成功，也应该返回success

        // 验证订单状态没有被更新
        assertEquals(0, order.getStatus());
        assertNull(order.getTradeNo());

        // 验证方法调用
        verify(paymentService).getOrderByOrderNo(orderNo);
        verify(paymentService, never()).updateById(any(PaymentOrder.class));
        verify(userMapper, never()).selectById(anyLong());
    }

    /**
     * 测试处理支付宝异步通知 - 验证签名关闭 - 订单已处理
     */
    @Test
    void testHandleAlipayNotify_WithoutSignVerification_OrderAlreadyProcessed() {
        // 1. 准备测试数据
        String orderNo = "TEST_ORDER_" + System.currentTimeMillis();
        PaymentOrder order = TestOrderUtil.createTestOrderWithOrderNo(orderNo);
        order.setStatus(1); // 已支付状态
        order.setTradeNo("EXISTING_TRADE_NO");

        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", orderNo);
        params.put("trade_no", "NEW_TRADE_NO");
        params.put("trade_status", "TRADE_SUCCESS");

        // 2. 配置Mock
        // 关闭签名验证
        ReflectionTestUtils.setField(paymentService, "verifyAlipayNotify", false);

        // 模拟查询订单成功
        doReturn(order).when(paymentService).getOrderByOrderNo(orderNo);

        // 3. 执行测试
        String result = paymentService.handleAlipayNotify(params);

        // 4. 验证结果
        assertEquals("success", result);

        // 验证订单状态没有被更新
        assertEquals(1, order.getStatus());
        assertEquals("EXISTING_TRADE_NO", order.getTradeNo());

        // 验证方法调用
        verify(paymentService).getOrderByOrderNo(orderNo);
        verify(paymentService, never()).updateById(any(PaymentOrder.class));
        verify(userMapper, never()).selectById(anyLong());
    }

    /**
     * 测试处理支付宝异步通知 - 验证签名关闭 - 用户不存在
     */
    @Test
    void testHandleAlipayNotify_WithoutSignVerification_UserNotFound() {
        // 1. 准备测试数据
        String orderNo = "TEST_ORDER_" + System.currentTimeMillis();
        PaymentOrder order = TestOrderUtil.createTestOrderWithOrderNo(orderNo);

        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", orderNo);
        params.put("trade_no", "2023071222001425901234567890");
        params.put("trade_status", "TRADE_SUCCESS");

        // 2. 配置Mock
        // 关闭签名验证
        ReflectionTestUtils.setField(paymentService, "verifyAlipayNotify", false);

        // 模拟查询订单成功
        doReturn(order).when(paymentService).getOrderByOrderNo(orderNo);

        // 模拟用户不存在
        when(userMapper.selectById(1L)).thenReturn(null);

        // 3. 执行测试
        String result = paymentService.handleAlipayNotify(params);

        // 4. 验证结果
        assertEquals("success", result); // 即使用户不存在，也应该返回success

        // 验证订单状态被更新
        assertEquals(1, order.getStatus());
        assertEquals("2023071222001425901234567890", order.getTradeNo());

        // 验证方法调用
        verify(paymentService).getOrderByOrderNo(orderNo);
        verify(paymentService).updateById(order);
        verify(userMapper).selectById(1L);
        verify(userMapper, never()).updateById(any(User.class));
    }

    /**
     * 测试处理支付宝异步通知 - 验证签名关闭 - 完整流程成功
     */
    @Test
    void testHandleAlipayNotify_WithoutSignVerification_FullSuccess() {
        // 1. 准备测试数据
        String orderNo = "TEST_ORDER_" + System.currentTimeMillis();
        PaymentOrder order = TestOrderUtil.createTestOrderWithOrderNo(orderNo);

        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", orderNo);
        params.put("trade_no", "2023071222001425901234567890");
        params.put("trade_status", "TRADE_SUCCESS");
        params.put("total_amount", "30.00");
        params.put("app_id", "2021000148638438");

        User user = new User();
        user.setId(1L);
        user.setUserType(0); // 普通用户

        // 2. 配置Mock
        // 关闭签名验证
        ReflectionTestUtils.setField(paymentService, "verifyAlipayNotify", false);

        // 模拟查询订单成功
        doReturn(order).when(paymentService).getOrderByOrderNo(orderNo);

        // 模拟查询用户成功
        when(userMapper.selectById(1L)).thenReturn(user);

        // 3. 执行测试
        String result = paymentService.handleAlipayNotify(params);

        // 4. 验证结果
        assertEquals("success", result);

        // 验证订单状态被更新
        assertEquals(1, order.getStatus());
        assertEquals("2023071222001425901234567890", order.getTradeNo());

        // 验证用户被更新为VIP
        assertEquals(1, user.getUserType());

        // 验证方法调用
        verify(paymentService).getOrderByOrderNo(orderNo);
        verify(userMapper).selectById(1L);
        verify(paymentService).updateById(order);
        verify(userMapper).updateById(user);
    }
}
