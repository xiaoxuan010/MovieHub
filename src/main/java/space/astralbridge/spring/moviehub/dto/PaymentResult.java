package space.astralbridge.spring.moviehub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResult {
    private String formHtml; // 支付宝表单HTML
} 