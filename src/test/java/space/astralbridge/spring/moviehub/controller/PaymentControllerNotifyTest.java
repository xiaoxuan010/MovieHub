package space.astralbridge.spring.moviehub.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import space.astralbridge.spring.moviehub.entity.PaymentOrder;
import space.astralbridge.spring.moviehub.service.PaymentService;
import space.astralbridge.spring.moviehub.util.TestOrderUtil;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 支付宝异步通知接口的详细集成测试
 */
public class PaymentControllerNotifyTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController)
                .alwaysDo(print()) // 打印请求和响应详情，便于调试
                .build();
    }

    /**
     * 测试支付宝异步通知 - 成功处理
     */
    @Test
    void testAlipayNotify_Success() throws Exception {
        // 1. 准备测试数据
        String orderNo = "TEST_ORDER_" + System.currentTimeMillis();
        PaymentOrder testOrder = TestOrderUtil.createTestOrderWithOrderNo(orderNo);
        
        // 2. 配置Mock
        when(paymentService.handleAlipayNotify(anyMap())).thenReturn("success");
        
        // 3. 执行测试
        mockMvc.perform(post("/api/payment/notify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("out_trade_no", orderNo)
                .param("trade_no", "2023071222001425901234567890")
                .param("trade_status", "TRADE_SUCCESS")
                .param("total_amount", "30.00")
                .param("app_id", "2021000148638438")
                .param("sign", "test_sign")
                .param("sign_type", "RSA2"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
        
        // 4. 验证方法调用
        verify(paymentService).handleAlipayNotify(anyMap());
    }

    /**
     * 测试支付宝异步通知 - 处理失败
     */
    @Test
    void testAlipayNotify_Failure() throws Exception {
        // 1. 准备测试数据
        String orderNo = "NONEXISTENT_ORDER";
        
        // 2. 配置Mock
        when(paymentService.handleAlipayNotify(anyMap())).thenReturn("failure");
        
        // 3. 执行测试
        mockMvc.perform(post("/api/payment/notify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("out_trade_no", orderNo)
                .param("trade_status", "TRADE_SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(content().string("failure"));
        
        // 4. 验证方法调用
        verify(paymentService).handleAlipayNotify(anyMap());
    }

    /**
     * 测试支付宝异步通知 - 参数转换
     * 验证控制器是否正确将请求参数转换为Map
     */
    @Test
    void testAlipayNotify_ParameterConversion() throws Exception {
        // 1. 准备测试数据
        String orderNo = "TEST_ORDER_" + System.currentTimeMillis();
        
        // 2. 配置Mock - 捕获传递给service的参数
        doAnswer(invocation -> {
            Map<String, String> params = invocation.getArgument(0);
            
            // 验证参数是否正确传递
            assert params.get("out_trade_no").equals(orderNo);
            assert params.get("trade_no").equals("2023071222001425901234567890");
            assert params.get("trade_status").equals("TRADE_SUCCESS");
            assert params.get("total_amount").equals("30.00");
            assert params.get("app_id").equals("2021000148638438");
            
            return "success";
        }).when(paymentService).handleAlipayNotify(anyMap());
        
        // 3. 执行测试
        mockMvc.perform(post("/api/payment/notify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("out_trade_no", orderNo)
                .param("trade_no", "2023071222001425901234567890")
                .param("trade_status", "TRADE_SUCCESS")
                .param("total_amount", "30.00")
                .param("app_id", "2021000148638438"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
        
        // 4. 验证方法调用
        verify(paymentService).handleAlipayNotify(anyMap());
    }

    /**
     * 测试支付宝异步通知 - 空参数
     */
    @Test
    void testAlipayNotify_EmptyParameters() throws Exception {
        // 1. 配置Mock
        when(paymentService.handleAlipayNotify(anyMap())).thenReturn("failure");
        
        // 2. 执行测试 - 不传递任何参数
        mockMvc.perform(post("/api/payment/notify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("failure"));
        
        // 3. 验证方法调用
        verify(paymentService).handleAlipayNotify(anyMap());
    }

    /**
     * 测试支付宝异步通知 - 异常处理
     */
    @Test
    void testAlipayNotify_ExceptionHandling() throws Exception {
        // 1. 配置Mock - 模拟服务抛出异常
        when(paymentService.handleAlipayNotify(anyMap())).thenThrow(new RuntimeException("测试异常"));
        
        // 2. 执行测试
        mockMvc.perform(post("/api/payment/notify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("out_trade_no", "TEST_ORDER")
                .param("trade_status", "TRADE_SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(content().string("failure")); // 应该返回failure
        
        // 3. 验证方法调用
        verify(paymentService).handleAlipayNotify(anyMap());
    }
}
