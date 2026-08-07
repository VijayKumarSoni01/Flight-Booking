package com.project.notificationmanagement.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.notificationmanagement.enums.CurrencyCode;
import com.project.notificationmanagement.enums.PaymentMethod;
import com.project.notificationmanagement.enums.PaymentStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFailedEmailRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid recipient email")
    private String recipientEmail;

    @NotBlank(message = "Booking reference is required")
    private String bookingReference;

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    @NotNull(message = "Payment amount is required")
    @Positive(message = "Payment amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    private CurrencyCode currency;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;

    @NotBlank(message = "Failure reason is required")
    private String failureReason;

    @NotBlank(message = "Retry payment URL is required")
    private String retryPaymentUrl;

    @NotNull(message = "Payment attempt time is required")
    private LocalDateTime attemptedAt;
}