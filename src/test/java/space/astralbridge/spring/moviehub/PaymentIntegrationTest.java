package space.astralbridge.spring.moviehub;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePagePayResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import space.astralbridge.spring.moviehub.entity.PaymentOrder;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.mapper.PaymentOrderMapper;
import space.astralbridge.spring.moviehub.mapper.UserMapper;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PaymentIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private AlipayClient alipayClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PaymentOrderMapper paymentOrderMapper;

    @Mock
    private AlipayTradePagePayResponse alipayResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
                
        // 设置支付宝客户端模拟返回
        when(alipayResponse.getBody()).thenReturn("<form>支付表单HTML</form>");
        try {
            when(alipayClient.pageExecute(any())).thenReturn(alipayResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testPaymentFlow() throws Exception {
        // 模拟AlipaySignature.rsaCheckV1总是返回true
        try (MockedStatic<AlipaySignature> mockedStatic = mockStatic(AlipaySignature.class)) {
            mockedStatic.when(() -> AlipaySignature.rsaCheckV1(
                    any(java.util.Map.class), anyString(), anyString(), anyString()
            )).thenReturn(true);

            // 1. 确保测试用户存在，ID为1
            User user = userMapper.selectById(1L);
            if (user == null) {
                user = new User();
                user.setId(1L); // 明确设置ID为1
                user.setUsername("testuser");
                user.setPassword("password");
                user.setEmail("test@example.com");
                user.setUserType(0);
                user.setStatus(1);
                userMapper.insert(user);
            } else {
                // 重置为非VIP并确保ID为1
                user.setId(1L);
                user.setUserType(0);
                userMapper.updateById(user);
            }

            // 2. 发送支付请求
            mockMvc.perform(post("/api/payment/pay/vip")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"duration\":\"monthly\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.formHtml", containsString("支付表单HTML")));

            // 3. 验证订单创建
            PaymentOrder latestOrder = null;
            for (PaymentOrder order : paymentOrderMapper.selectList(null)) {
                if (latestOrder == null || order.getId() > latestOrder.getId()) {
                    latestOrder = order;
                }
            }
            
            assertNotNull(latestOrder);
            assertEquals(0, latestOrder.getStatus()); // 待支付状态
            // 验证订单关联的用户ID是否为1
            assertEquals(1L, latestOrder.getUserId());
            String orderNo = latestOrder.getOrderNo();

            // 4. 模拟支付回调 - 将断言分开执行
            mockMvc.perform(post("/api/payment/notify")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("out_trade_no", orderNo)
                    .param("trade_no", "2023071222001425901234567890")
                    .param("trade_status", "TRADE_SUCCESS")
                    .param("sign", "dummy_sign")
                    .param("sign_type", "RSA2"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("success"));

            // 5. 手动验证订单和用户状态更新
            User updatedUser = userMapper.selectById(user.getId());
            PaymentOrder updatedOrder = paymentOrderMapper.selectById(latestOrder.getId());
            
            assertNotNull(updatedOrder);
            assertNotNull(updatedUser);
            assertEquals(1, updatedOrder.getStatus()); // 支付成功
            assertEquals("2023071222001425901234567890", updatedOrder.getTradeNo());
            assertEquals(1, updatedUser.getUserType()); // 变成VIP用户
        }
    }
} 