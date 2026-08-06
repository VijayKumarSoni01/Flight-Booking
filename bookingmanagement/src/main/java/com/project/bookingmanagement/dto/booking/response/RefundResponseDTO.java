package com.project.bookingmanagement.dto.booking.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;

import lombok.Data;

@Data
public class RefundResponseDTO {

    private Long paymentId;

    private BigDecimal refundAmount;

    private PaymentStatus paymentStatus;

    private String gatewayRefundId;

    private LocalDateTime refundedAt;

    private String message;
}