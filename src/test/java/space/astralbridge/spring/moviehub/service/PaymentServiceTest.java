package space.astralbridge.spring.moviehub.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import space.astralbridge.spring.moviehub.config.AlipayConfig;
import space.astralbridge.spring.moviehub.dto.PaymentRequest;
import space.astralbridge.spring.moviehub.entity.PaymentOrder;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.mapper.PaymentOrderMapper;
import space.astralbridge.spring.moviehub.mapper.UserMapper;
import space.astralbridge.spring.moviehub.service.impl.PaymentServiceImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentServiceTest {

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
        // 使用spy创建一个可以部分mock的实例
        paymentService = spy(new PaymentServiceImpl(alipayClient, alipayConfig, userMapper));
        ReflectionTestUtils.setField(paymentService, "baseMapper", paymentOrderMapper);
        ReflectionTestUtils.setField(paymentService, "alipayPublicKey", "test_public_key");
        ReflectionTestUtils.setField(paymentService, "signType", "RSA2");
        // 不再需要设置verifyAlipayNotify，因为相关功能已移除
    }

    @Test
    void testCreateAlipayForm() throws AlipayApiException {
        // 准备测试数据
        Long userId = 1L;
        PaymentRequest request = new PaymentRequest();
        request.setDuration("monthly");
        BigDecimal monthlyPrice = new BigDecimal("30.00");
        
        // 创建模拟用户
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        
        // 配置模拟行为
        when(userMapper.selectById(userId)).thenReturn(user);
        when(alipayConfig.getVipPrice("monthly")).thenReturn(monthlyPrice);
        when(alipayConfig.getReturnUrl()).thenReturn("http://test.com/return");
        when(paymentOrderMapper.insert(any(PaymentOrder.class))).thenReturn(1);
        
        AlipayTradePagePayResponse mockResponse = mock(AlipayTradePagePayResponse.class);
        when(mockResponse.getBody()).thenReturn("<form>支付表单内容</form>");
        when(alipayClient.pageExecute(any(AlipayTradePagePayRequest.class))).thenReturn(mockResponse);
        
        // 执行测试
        String result = paymentService.createAlipayForm(userId, request);
        
        // 验证结果
        assertEquals("<form>支付表单内容</form>", result);
        
        // 验证用户查询
        verify(userMapper).selectById(userId);
        
        // 验证订单是否被保存
        ArgumentCaptor<PaymentOrder> orderCaptor = ArgumentCaptor.forClass(PaymentOrder.class);
        verify(paymentOrderMapper).insert(orderCaptor.capture());
        
        PaymentOrder savedOrder = orderCaptor.getValue();
        assertEquals(userId, savedOrder.getUserId());
        assertEquals(monthlyPrice, savedOrder.getAmount());
        assertEquals("monthly", savedOrder.getVipDuration());
        assertEquals("alipay", savedOrder.getPayType());
        assertEquals(0, savedOrder.getStatus());
        assertNotNull(savedOrder.getOrderNo());
    }
    
    @Test
    void testGetOrderByOrderNo() {
        // 准备测试数据
        String orderNo = "202307120123451234";
        PaymentOrder order = new PaymentOrder();
        order.setId(1L);
        order.setOrderNo(orderNo);
        
        // Mock查询 - 使用spy的特性直接mock同一个类的方法
        doReturn(order).when(paymentService).getOne(any());
        
        // 执行测试
        PaymentOrder result = paymentService.getOrderByOrderNo(orderNo);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(orderNo, result.getOrderNo());
    }
    
} 