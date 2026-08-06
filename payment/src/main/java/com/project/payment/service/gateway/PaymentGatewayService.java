package com.project.payment.service.gateway;

import com.project.payment.dto.request.RefundPaymentReqDTO;
import com.project.payment.dto.request.VerifyPaymentReqDTO;
import com.project.payment.dto.response.PaymentConfirmationResDTO;
import com.project.payment.dto.response.RefundResponseDTO;
import com.project.payment.entity.Payment;
import com.project.payment.enums.PaymentGateway;

public interface PaymentGatewayService {

    PaymentGateway getGateway();

    PaymentConfirmationResDTO createPayment(Payment payment);

    void verifyPayment(
            Payment payment,
            VerifyPaymentReqDTO request);

    RefundResponseDTO refundPayment(
            Payment payment,
            RefundPaymentReqDTO request);
}