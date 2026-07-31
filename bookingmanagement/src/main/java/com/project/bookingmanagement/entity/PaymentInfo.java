package com.project.bookingmanagement.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.bookingmanagement.enums.paymentInfoEnum.PaymentMethod;
import com.project.bookingmanagement.enums.paymentInfoEnum.PaymentProvider;
import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_info", indexes = {
        @Index(name = "idx_payment_transaction", columnList = "transactionId"),
        @Index(name = "idx_payment_booking", columnList = "booking_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Payment status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @NotNull(message = "Payment method is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment provider is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentProvider paymentProvider;

    @Size(max = 100)
    @Column(unique = true, length = 100)
    private String transactionId;

    @Size(max = 100)
    @Column(length = 100)
    private String paymentReference;

    @Size(max = 100)
    @Column(length = 100)
    private String gatewayPaymentId;

    @Column(length = 100)
    private String gatewayOrderId;

    @Column(length = 100)
    private String gatewaySignature;

    @Size(max = 500)
    @Column(length = 500)
    private String failureReason;

    @Column
    private LocalDateTime paymentTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (paymentTime == null && paymentStatus == PaymentStatus.SUCCESS) {
            paymentTime = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();

        if (paymentTime == null && paymentStatus == PaymentStatus.SUCCESS) {
            paymentTime = LocalDateTime.now();
        }
    }
}