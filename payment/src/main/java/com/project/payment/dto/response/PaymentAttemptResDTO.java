package com.project.payment.dto.response;

import java.time.LocalDateTime;

import com.project.payment.enums.PaymentGateway;
import com.project.payment.enums.PaymentMethod;
import com.project.payment.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentAttemptResDTO {

    private Integer attemptNumber;

    private PaymentStatus paymentStatus;

    private PaymentGateway paymentGateway;

    private PaymentMethod paymentMethod;

    private LocalDateTime createdAt;

    private String failureReason;
}
