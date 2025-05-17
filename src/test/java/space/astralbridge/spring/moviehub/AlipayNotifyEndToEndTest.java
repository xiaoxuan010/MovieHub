package space.astralbridge.spring.moviehub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import space.astralbridge.spring.moviehub.entity.PaymentOrder;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.mapper.PaymentOrderMapper;
import space.astralbridge.spring.moviehub.mapper.UserMapper;
import space.astralbridge.spring.moviehub.service.impl.PaymentServiceImpl;
import space.astralbridge.spring.moviehub.util.TestOrderUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 支付宝异步通知的端到端测试
 * 测试整个流程，从接收通知到更新订单和用户状态
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional // 使用事务，测试完成后自动回滚，不影响数据库
public class AlipayNotifyEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PaymentServiceImpl paymentService;

    // 测试订单号
    private static final String TEST_ORDER_NO = "E2E_TEST_ORDER_" + System.currentTimeMillis();

    // 测试用户ID
    private static final Long TEST_USER_ID = 1L;

    private PaymentOrder testOrder;
    private User testUser;

    @BeforeEach
    void setUp() {
        // 关闭签名验证，便于测试
        org.springframework.test.util.ReflectionTestUtils.setField(paymentService, "verifyAlipayNotify", false);
        
        // 准备测试用户
        testUser = userMapper.selectById(TEST_USER_ID);
        if (testUser == null) {
            // 如果测试用户不存在，创建一个
            testUser = new User();
            testUser.setId(TEST_USER_ID);
            testUser.setUsername("testuser");
            testUser.setPassword("password");
            testUser.setEmail("test@example.com");
            testUser.setUserType(0); // 普通用户
            testUser.setStatus(1); // 启用
            testUser.setCreateTime(LocalDateTime.now());
            testUser.setUpdateTime(LocalDateTime.now());
            userMapper.insert(testUser);
        } else {
            // 如果测试用户存在，确保是普通用户
            testUser.setUserType(0);
            userMapper.updateById(testUser);
        }
        
        // 创建测试订单
        testOrder = new PaymentOrder();
        testOrder.setOrderNo(TEST_ORDER_NO);
        testOrder.setUserId(TEST_USER_ID);
        testOrder.setAmount(new BigDecimal("30.00"));
        testOrder.setStatus(0); // 待支付状态
        testOrder.setPayType("alipay");
        testOrder.setVipDuration("monthly");
        testOrder.setCreateTime(LocalDateTime.now());
        testOrder.setUpdateTime(LocalDateTime.now());
        paymentOrderMapper.insert(testOrder);
    }

    /**
     * 测试支付宝异步通知 - 完整流程
     * 1. 发送异步通知请求
     * 2. 验证订单状态更新
     * 3. 验证用户升级为VIP
     */
    @Test
    void testAlipayNotify_FullProcess() throws Exception {
        // 1. 执行测试 - 发送异步通知请求
        mockMvc.perform(post("/api/payment/notify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("out_trade_no", TEST_ORDER_NO)
                .param("trade_no", "2023071222001425901234567890")
                .param("trade_status", "TRADE_SUCCESS")
                .param("total_amount", "30.00")
                .param("app_id", "2021000148638438")
                .param("sign", "test_sign")
                .param("sign_type", "RSA2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
        
        // 2. 验证订单状态更新
        PaymentOrder updatedOrder = paymentOrderMapper.selectById(testOrder.getId());
        assertNotNull(updatedOrder);
        assertEquals(1, updatedOrder.getStatus()); // 状态应该更新为已支付
        assertEquals("2023071222001425901234567890", updatedOrder.getTradeNo()); // 交易号应该更新
        
        // 3. 验证用户升级为VIP
        User updatedUser = userMapper.selectById(TEST_USER_ID);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getUserType()); // 用户类型应该更新为VIP
    }

    /**
     * 测试支付宝异步通知 - 订单不存在
     */
    @Test
    void testAlipayNotify_OrderNotFound() throws Exception {
        // 1. 执行测试 - 发送异步通知请求，使用不存在的订单号
        mockMvc.perform(post("/api/payment/notify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("out_trade_no", "NONEXISTENT_ORDER")
                .param("trade_no", "2023071222001425901234567890")
                .param("trade_status", "TRADE_SUCCESS"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("failure"));
        
        // 2. 验证用户状态没有变化
        User updatedUser = userMapper.selectById(TEST_USER_ID);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getUserType()); // 用户类型应该保持不变
    }

    /**
     * 测试支付宝异步通知 - 交易状态不是成功
     */
    @Test
    void testAlipayNotify_TradeStatusNotSuccess() throws Exception {
        // 1. 执行测试 - 发送异步通知请求，交易状态为TRADE_CLOSED
        mockMvc.perform(post("/api/payment/notify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("out_trade_no", TEST_ORDER_NO)
                .param("trade_no", "2023071222001425901234567890")
                .param("trade_status", "TRADE_CLOSED"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("success")); // 应该返回success
        
        // 2. 验证订单状态没有变化
        PaymentOrder updatedOrder = paymentOrderMapper.selectById(testOrder.getId());
        assertNotNull(updatedOrder);
        assertEquals(0, updatedOrder.getStatus()); // 状态应该保持不变
        assertNull(updatedOrder.getTradeNo()); // 交易号应该保持不变
        
        // 3. 验证用户状态没有变化
        User updatedUser = userMapper.selectById(TEST_USER_ID);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getUserType()); // 用户类型应该保持不变
    }

    /**
     * 测试支付宝异步通知 - 订单已处理
     */
    @Test
    void testAlipayNotify_OrderAlreadyProcessed() throws Exception {
        // 1. 先将订单标记为已处理
        testOrder.setStatus(1); // 已支付
        testOrder.setTradeNo("EXISTING_TRADE_NO");
        paymentOrderMapper.updateById(testOrder);
        
        // 2. 执行测试 - 发送异步通知请求
        mockMvc.perform(post("/api/payment/notify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("out_trade_no", TEST_ORDER_NO)
                .param("trade_no", "NEW_TRADE_NO")
                .param("trade_status", "TRADE_SUCCESS"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("success")); // 应该返回success
        
        // 3. 验证订单状态没有变化
        PaymentOrder updatedOrder = paymentOrderMapper.selectById(testOrder.getId());
        assertNotNull(updatedOrder);
        assertEquals(1, updatedOrder.getStatus()); // 状态应该保持不变
        assertEquals("EXISTING_TRADE_NO", updatedOrder.getTradeNo()); // 交易号应该保持不变
    }

    /**
     * 测试支付宝异步通知 - 重复通知
     */
    @Test
    void testAlipayNotify_DuplicateNotification() throws Exception {
        // 1. 第一次通知
        mockMvc.perform(post("/api/payment/notify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("out_trade_no", TEST_ORDER_NO)
                .param("trade_no", "2023071222001425901234567890")
                .param("trade_status", "TRADE_SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
        
        // 验证订单和用户状态已更新
        PaymentOrder updatedOrder = paymentOrderMapper.selectById(testOrder.getId());
        assertEquals(1, updatedOrder.getStatus());
        User updatedUser = userMapper.selectById(TEST_USER_ID);
        assertEquals(1, updatedUser.getUserType());
        
        // 2. 第二次通知（重复通知）
        mockMvc.perform(post("/api/payment/notify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("out_trade_no", TEST_ORDER_NO)
                .param("trade_no", "2023071222001425901234567890")
                .param("trade_status", "TRADE_SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(content().string("success")); // 应该返回success
        
        // 验证订单和用户状态没有变化
        updatedOrder = paymentOrderMapper.selectById(testOrder.getId());
        assertEquals(1, updatedOrder.getStatus());
        updatedUser = userMapper.selectById(TEST_USER_ID);
        assertEquals(1, updatedUser.getUserType());
    }
}
