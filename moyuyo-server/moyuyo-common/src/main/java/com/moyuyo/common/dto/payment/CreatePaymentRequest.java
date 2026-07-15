package com.moyuyo.common.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "发起支付请求")
public class CreatePaymentRequest {

    @NotBlank(message = "订单号不能为空")
    @Schema(description = "订单号", example = "ORD2026071500000001")
    private String orderNo;

    @NotBlank(message = "支付渠道不能为空")
    @Schema(description = "支付渠道: STRIPE / PAYPAL", example = "STRIPE")
    private String payChannel;

    @Schema(description = "成功后的跳转URL", example = "https://moyuyo.com/order/success")
    private String returnUrl;
}
