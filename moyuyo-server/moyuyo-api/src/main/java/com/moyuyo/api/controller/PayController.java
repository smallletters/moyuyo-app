package com.moyuyo.api.controller;

import com.moyuyo.common.Result;
import com.moyuyo.common.dto.payment.CreatePaymentRequest;
import com.moyuyo.common.dto.payment.CreatePaymentResponse;
import com.moyuyo.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "支付管理")
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PayController {

    private final PaymentService paymentService;

    @Operation(summary = "发起支付")
    @PostMapping("/create")
    public Result<CreatePaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        return Result.success(paymentService.createPayment(request));
    }

    @Operation(summary = "Stripe Webhook 回调")
    @PostMapping("/stripe/webhook")
    public Result<Void> stripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {
        paymentService.handleWebhook("STRIPE", payload, signature);
        return Result.success();
    }

    @Operation(summary = "PayPal Webhook 回调")
    @PostMapping("/paypal/webhook")
    public Result<Void> paypalWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "PAYPAL-TRANSMISSION-SIG", required = false) String signature) {
        paymentService.handleWebhook("PAYPAL", payload, signature);
        return Result.success();
    }
}
