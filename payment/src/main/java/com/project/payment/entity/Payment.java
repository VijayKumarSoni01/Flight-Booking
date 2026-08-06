package com.project.payment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.payment.enums.CurrencyCode;
import com.project.payment.enums.PaymentGateway;
import com.project.payment.enums.PaymentMethod;
import com.project.payment.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_booking_reference", columnList = "booking_reference"),
        @Index(name = "idx_booking_id", columnList = "booking_id"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_transaction_id", columnList = "transaction_id"),
        @Index(name = "idx_gateway_order_id", columnList = "gateway_order_id"),
        @Index(name = "idx_gateway_payment_id", columnList = "gateway_payment_id"),
        @Index(name = "idx_payment_status", columnList = "payment_status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Booking id is required")
    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "gateway_refund_id", length = 100)
    private String gatewayRefundId;

    @Column(name = "gateway_customer_id")
    private String gatewayCustomerId;

    @NotBlank(message = "Booking reference is required")
    @Size(max = 30)
    @Column(name = "booking_reference", nullable = false, length = 30)
    private String bookingReference;

    @NotNull(message = "User id is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CurrencyCode currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_gateway", nullable = false, length = 30)
    private PaymentGateway paymentGateway;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    private PaymentStatus paymentStatus;

    @Column(name = "gateway_order_id", unique = true, length = 100)
    private String gatewayOrderId;

    @Column(name = "gateway_payment_id", unique = true, length = 100)
    private String gatewayPaymentId;

    @Column(name = "transaction_id", unique = true, length = 100)
    private String transactionId;

    @Column(name = "gateway_error_code", length = 100)
    private String gatewayErrorCode;

    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    @Column(name = "gateway_response", columnDefinition = "TEXT")
    private String gatewayResponse;

    @Column(name = "receipt_url", length = 500)
    private String receiptUrl;

    @Column(length = 255)
    private String description;

    @Column(name = "idempotency_key", unique = true, length = 100)
    private String idempotencyKey;

    @Builder.Default
    @Column(name = "webhook_verified")
    private Boolean webhookVerified = false;

    @Column(name = "processing_time_ms")
    private Long processingTimeMs;

    @Builder.Default
    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Builder.Default
    @Column(name = "attempt_number")
    private Integer attemptNumber = 1;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "refund_amount", precision = 12, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "refunded_at")
    private LocalDateTime refundedAt;

    @Column(length = 500)
    private String remarks;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @PrePersist
    public void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }

        if (retryCount == null) {
            retryCount = 0;
        }

        if (attemptNumber == null) {
            attemptNumber = 1;
        }

        if (webhookVerified == null) {
            webhookVerified = false;
        }
    }

    @PreUpdate
    public void onUpdate() {

        this.updatedAt = LocalDateTime.now();

        if (paymentStatus == PaymentStatus.SUCCESS && paidAt == null) {
            paidAt = LocalDateTime.now();
        }

        if (paymentStatus == PaymentStatus.REFUNDED && refundedAt == null) {
            refundedAt = LocalDateTime.now();
        }
    }
}