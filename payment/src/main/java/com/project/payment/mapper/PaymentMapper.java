package com.project.payment.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.project.payment.dto.request.CreatePaymentReqDTO;
import com.project.payment.dto.response.PaymentConfirmationResDTO;
import com.project.payment.dto.response.PaymentResDTO;
import com.project.payment.dto.response.PaymentStatusResDTO;
import com.project.payment.dto.response.PaymentSummaryResDTO;
import com.project.payment.dto.response.RefundResponseDTO;
import com.project.payment.entity.Payment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PaymentMapper {

    // Request DTO -> Entity

    @Mapping(target = "id", ignore = true)

    @Mapping(target = "bookingId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "currency", ignore = true)

    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "gatewayCustomerId", ignore = true)
    @Mapping(target = "gatewayRefundId", ignore = true)
    @Mapping(target = "gatewayOrderId", ignore = true)
    @Mapping(target = "gatewayPaymentId", ignore = true)
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "gatewayErrorCode", ignore = true)
    @Mapping(target = "failureReason", ignore = true)
    @Mapping(target = "gatewayResponse", ignore = true)
    @Mapping(target = "receiptUrl", ignore = true)
    @Mapping(target = "idempotencyKey", ignore = true)
    @Mapping(target = "webhookVerified", ignore = true)
    @Mapping(target = "processingTimeMs", ignore = true)
    @Mapping(target = "retryCount", ignore = true)
    @Mapping(target = "attemptNumber", ignore = true)
    @Mapping(target = "paidAt", ignore = true)
    @Mapping(target = "refundAmount", ignore = true)
    @Mapping(target = "refundedAt", ignore = true)
    @Mapping(target = "remarks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Payment toEntity(CreatePaymentReqDTO dto);

    // Entity -> Payment Response

    PaymentResDTO toPaymentResDTO(Payment payment);

    List<PaymentResDTO> toPaymentResDTOList(List<Payment> payments);

    // Entity -> Payment Summary

    PaymentSummaryResDTO toPaymentSummaryResDTO(Payment payment);

    List<PaymentSummaryResDTO> toPaymentSummaryResDTOList(List<Payment> payments);

    // Entity -> Payment Status

    PaymentStatusResDTO toPaymentStatusResDTO(Payment payment);

    // Entity -> Payment Confirmation

    @Mapping(source = "id", target = "paymentId")
    @Mapping(target = "paymentUrl", ignore = true)
    @Mapping(target = "keyId", ignore = true)
    @Mapping(target = "clientSecret", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    PaymentConfirmationResDTO toPaymentConfirmationResDTO(Payment payment);

    // Entity -> Refund Response

    @Mapping(source = "id", target = "paymentId")
    @Mapping(source = "gatewayRefundId", target = "gatewayRefundId")
    @Mapping(target = "message", ignore = true)
    RefundResponseDTO toRefundResponseDTO(Payment payment);
}
