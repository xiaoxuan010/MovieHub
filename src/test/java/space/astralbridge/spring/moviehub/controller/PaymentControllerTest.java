package space.astralbridge.spring.moviehub.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import space.astralbridge.spring.moviehub.dto.PaymentRequest;
import space.astralbridge.spring.moviehub.security.UserDetailsImpl;
import space.astralbridge.spring.moviehub.service.PaymentService;
import space.astralbridge.spring.moviehub.entity.User;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;
    
    @Mock
    private SecurityContext securityContext;
    
    @Mock
    private Authentication authentication;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void testPayVip() throws Exception {
        // 准备测试数据
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        
        // 配置安全上下文
        when(authentication.getPrincipal()).thenReturn(userDetails);
        
        // Mock支付服务
        String payformHtml = "<form>测试表单内容</form>";
        when(paymentService.createAlipayForm(eq(1L), any(PaymentRequest.class))).thenReturn(payformHtml);
        
        // 执行测试
        mockMvc.perform(post("/api/payment/pay/vip")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"duration\":\"monthly\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.message", is("请在浏览器中提交此表单以完成支付")))
                .andExpect(jsonPath("$.data.formHtml", is(payformHtml)));
        
        // 验证服务调用
        verify(paymentService).createAlipayForm(eq(1L), any(PaymentRequest.class));
    }
    
    @Test
    void testAlipayReturn() throws Exception {
        // 执行测试
        mockMvc.perform(get("/api/payment/return")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("支付已完成，正在跳转回电影网站..."));
    }
    
    @Test
    void testAlipayNotify() throws Exception {
        // 准备测试数据
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("out_trade_no", "202307120123451234");
        request.setParameter("trade_no", "2023071222001425901234567890");
        request.setParameter("trade_status", "TRADE_SUCCESS");
        
        // Mock支付服务
        when(paymentService.handleAlipayNotify(anyMap())).thenReturn("success");
        
        // 执行测试
        mockMvc.perform(post("/api/payment/notify")
                .param("out_trade_no", "202307120123451234")
                .param("trade_no", "2023071222001425901234567890")
                .param("trade_status", "TRADE_SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
        
        // 验证服务调用
        verify(paymentService).handleAlipayNotify(anyMap());
    }
} 