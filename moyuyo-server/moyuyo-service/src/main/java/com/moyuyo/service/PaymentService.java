package com.moyuyo.service;

import com.moyuyo.common.dto.payment.CreatePaymentRequest;
import com.moyuyo.common.dto.payment.CreatePaymentResponse;

public interface PaymentService {

    CreatePaymentResponse createPayment(CreatePaymentRequest request);

    void handleWebhook(String payChannel, String payload, String signature);
}
