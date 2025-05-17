package space.astralbridge.spring.moviehub.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

import lombok.Getter;

@Configuration
public class AlipayConfig {
    @Getter
    @Value("${alipay.app_id}")
    private String appId;
    
    @Value("${alipay.merchant_private_key}")
    private String merchantPrivateKey;
    
    @Getter
    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;
    
    @Getter
    @Value("${alipay.gateway_url}")
    private String gatewayUrl;
    
    @Getter
    @Value("${alipay.sign_type}")
    private String signType;
    
    @Getter
    @Value("${alipay.notify_url}")
    private String notifyUrl;
    
    @Getter
    @Value("${alipay.return_url}")
    private String returnUrl;
    
    @Value("${alipay.vip_prices.monthly}")
    private BigDecimal monthlyPrice;
    
    @Value("${alipay.vip_prices.yearly}")
    private BigDecimal yearlyPrice;
    
    @Bean
    public AlipayClient alipayClient() {
        // 使用最佳实践初始化AlipayClient
        return new DefaultAlipayClient(
            gatewayUrl, 
            appId, 
            merchantPrivateKey, 
            "json",  // 明确指定JSON格式
            "UTF-8", 
            alipayPublicKey, 
            signType
        );
    }

    public BigDecimal getVipPrice(String duration) {
        if ("yearly".equalsIgnoreCase(duration)) {
            return yearlyPrice;
        }
        return monthlyPrice; // 默认返回月度价格
    }
} 