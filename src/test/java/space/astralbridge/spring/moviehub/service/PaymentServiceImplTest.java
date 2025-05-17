package space.astralbridge.spring.moviehub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.alipay.api.AlipayClient;

import space.astralbridge.spring.moviehub.config.AlipayConfig;
import space.astralbridge.spring.moviehub.entity.PaymentOrder;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.mapper.PaymentOrderMapper;
import space.astralbridge.spring.moviehub.mapper.UserMapper;
import space.astralbridge.spring.moviehub.service.impl.PaymentServiceImpl;

public class PaymentServiceImplTest {

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
        MockitoAnnotations.openMocks(this);
        paymentService = spy(new PaymentServiceImpl(alipayClient, alipayConfig, userMapper));
        ReflectionTestUtils.setField(paymentService, "baseMapper", paymentOrderMapper);
        ReflectionTestUtils.setField(paymentService, "verifyAlipayNotify", false);
    }

    @Test
    void testHandleAlipayNotify() {
        // 1. 准备测试数据
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", "test123");
        params.put("trade_no", "alipay123");
        params.put("trade_status", "TRADE_SUCCESS");
        
        PaymentOrder order = new PaymentOrder();
        order.setId(1L);
        order.setOrderNo("test123");
        order.setUserId(1L);
        order.setStatus(0);
        
        User user = new User();
        user.setId(1L);
        user.setUserType(0);
        
        // 2. 设置Mock
        doReturn(order).when(paymentService).getOrderByOrderNo(anyString());
        when(userMapper.selectById(1L)).thenReturn(user);
        
        // 3. 执行测试
        String result = paymentService.handleAlipayNotify(params);
        
        // 4. 验证结果
        assertEquals("success", result);
        assertEquals(1, order.getStatus());
        assertEquals("alipay123", order.getTradeNo());
        assertEquals(1, user.getUserType());
        
        // 5. 验证交互
        verify(paymentOrderMapper).updateById(order);
        verify(userMapper).updateById(user);
    }
} 