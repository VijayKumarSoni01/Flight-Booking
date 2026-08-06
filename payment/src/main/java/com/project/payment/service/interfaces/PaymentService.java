package com.project.payment.service.interfaces;

import java.util.List;

import com.project.payment.dto.request.CreatePaymentReqDTO;
import com.project.payment.dto.request.RefundPaymentReqDTO;
import com.project.payment.dto.request.RetryPaymentReqDTO;
import com.project.payment.dto.request.VerifyPaymentReqDTO;
import com.project.payment.dto.response.PaymentConfirmationResDTO;
import com.project.payment.dto.response.PaymentResDTO;
import com.project.payment.dto.response.PaymentStatusResDTO;
import com.project.payment.dto.response.PaymentSummaryResDTO;
import com.project.payment.dto.response.RefundResponseDTO;

public interface PaymentService {

    PaymentConfirmationResDTO createPayment(CreatePaymentReqDTO request);

    PaymentStatusResDTO verifyPayment(VerifyPaymentReqDTO request);

    RefundResponseDTO refundPayment(RefundPaymentReqDTO request);

    PaymentConfirmationResDTO retryPayment(RetryPaymentReqDTO request);

    PaymentResDTO getPaymentById(Long paymentId);

    List<PaymentSummaryResDTO> getPaymentsByBookingReference(String bookingReference);

    PaymentStatusResDTO getPaymentStatus(String bookingReference);
}
