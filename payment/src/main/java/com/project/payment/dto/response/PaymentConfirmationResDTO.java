package com.project.payment.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.payment.enums.CurrencyCode;
import com.project.payment.enums.PaymentGateway;
import com.project.payment.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentConfirmationResDTO {

    private Long paymentId;
    private String bookingReference;
    private String gatewayOrderId;
    private PaymentStatus paymentStatus;
    private BigDecimal amount;
    private CurrencyCode currency;
    private PaymentGateway paymentGateway;
    private String paymentUrl;
    private String keyId;
    private LocalDateTime expiresAt;
    private String clientSecret;
}
