package com.project.notificationmanagement.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.notificationmanagement.enums.CurrencyCode;
import com.project.notificationmanagement.enums.PaymentGateway;
import com.project.notificationmanagement.enums.PaymentMethod;
import com.project.notificationmanagement.enums.RefundStatus;

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
public class RefundEmailRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid recipient email")
    private String recipientEmail;

    @NotBlank(message = "Booking reference is required")
    private String bookingReference;

    @NotBlank(message = "PNR is required")
    private String pnr;

    @NotBlank(message = "Airline name is required")
    private String airlineName;

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Payment ID is required")
    private String paymentId;

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    @NotNull(message = "Refund amount is required")
    @Positive(message = "Refund amount must be greater than zero")
    private BigDecimal refundAmount;

    @NotNull(message = "Currency is required")
    private CurrencyCode currency;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment gateway is required")
    private PaymentGateway paymentGateway;

    @NotNull(message = "Refund status is required")
    private RefundStatus refundStatus;

    @NotBlank(message = "Refund reference is required")
    private String refundReference;

    @NotBlank(message = "Refund reason is required")
    private String refundReason;

    @NotNull(message = "Refund initiation time is required")
    private LocalDateTime refundInitiatedAt;

    @NotBlank(message = "Application name is required")
    private String applicationName;

    @NotBlank(message = "Support email is required")
    @Email(message = "Invalid support email")
    private String supportEmail;
}