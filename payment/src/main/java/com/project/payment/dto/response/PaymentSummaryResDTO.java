package com.project.payment.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.payment.enums.PaymentGateway;
import com.project.payment.enums.PaymentMethod;
import com.project.payment.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentSummaryResDTO {

    private Long id;
    private String bookingReference;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private PaymentGateway paymentGateway;
    private LocalDateTime paidAt;
}