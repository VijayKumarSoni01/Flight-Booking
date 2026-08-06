package com.project.payment.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.payment.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefundResponseDTO {

    private Long paymentId;

    private BigDecimal refundAmount;

    private PaymentStatus paymentStatus;

    private String gatewayRefundId;

    private LocalDateTime refundedAt;

    private String message;
}