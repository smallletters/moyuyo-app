package com.moyuyo.common.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "支付响应")
public class CreatePaymentResponse {

    @Schema(description = "支付会话ID", example = "pi_3MqF9T2eZvKYlo2C0XjZf8xR")
    private String paymentId;

    @Schema(description = "客户端密钥(Stripe)", example = "pi_3MqF9T2eZvKYlo2C0XjZf8xR_secret_xyz")
    private String clientSecret;

    @Schema(description = "支付页面URL(PayPal)", example = "https://www.paypal.com/checkout?token=EC-123456")
    private String approvalUrl;

    @Schema(description = "支付渠道: STRIPE / PAYPAL", example = "STRIPE")
    private String payChannel;
}
