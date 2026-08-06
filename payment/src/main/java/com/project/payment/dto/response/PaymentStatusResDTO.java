package com.project.payment.dto.response;

import java.time.LocalDateTime;

import com.project.payment.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentStatusResDTO {

    private String bookingReference;
    private PaymentStatus paymentStatus;
    private String gatewayPaymentId;
    private String transactionId;
    private Integer attemptNumber;
    private LocalDateTime paidAt;
}
