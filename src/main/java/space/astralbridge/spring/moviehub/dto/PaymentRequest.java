package space.astralbridge.spring.moviehub.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    // VIP时长：monthly/yearly，默认为monthly
    private String duration = "monthly";
} 