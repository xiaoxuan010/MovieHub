package space.astralbridge.spring.moviehub.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Test
    void testAlipaySignatureVerification() throws AlipayApiException {
        try (MockedStatic<AlipaySignature> mockedStatic = mockStatic(AlipaySignature.class)) {
            // 模拟支付宝签名验证
            mockedStatic.when(() -> AlipaySignature.rsaCheckV1(
                    anyMap(), anyString(), anyString(), anyString()
            )).thenReturn(true);

            // 创建测试参数
            Map<String, String> params = new HashMap<>();
            params.put("out_trade_no", "test_order_no");
            params.put("trade_status", "TRADE_SUCCESS");
            params.put("sign", "test_sign");

            // 验证签名验证返回true
            boolean result = AlipaySignature.rsaCheckV1(params, "test_key", "UTF-8", "RSA2");
            assertTrue(result);

            // 修改返回值为false
            mockedStatic.when(() -> AlipaySignature.rsaCheckV1(
                    anyMap(), anyString(), anyString(), anyString()
            )).thenReturn(false);

            // 验证签名验证返回false
            result = AlipaySignature.rsaCheckV1(params, "test_key", "UTF-8", "RSA2");
            assertFalse(result);
        }
    }
} 