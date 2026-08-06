package com.project.payment.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.payment.enums.CurrencyCode;
import com.project.payment.enums.PaymentGateway;
import com.project.payment.enums.PaymentMethod;
import com.project.payment.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResDTO {

    private Long id;
    private Long bookingId;
    private String bookingReference;
    private Long userId;
    private BigDecimal amount;
    private CurrencyCode currency;
    private PaymentMethod paymentMethod;
    private PaymentGateway paymentGateway;
    private PaymentStatus paymentStatus;
    private String gatewayOrderId;
    private String gatewayPaymentId;
    private String transactionId;
    private String receiptUrl;
    private Integer attemptNumber;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}