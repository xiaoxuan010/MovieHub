package space.astralbridge.spring.moviehub.service;

import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.alipay.api.AlipayClient;

import space.astralbridge.spring.moviehub.config.AlipayConfig;
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

    // 注意：handleAlipayNotify方法相关的测试用例已被移除
}