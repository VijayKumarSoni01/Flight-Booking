package com.project.bookingmanagement.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.project.bookingmanagement.enums.bookingEnum.BookingStatus;
import com.project.bookingmanagement.enums.bookingEnum.CurrencyCode;
import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;
import com.project.bookingmanagement.enums.bookingPassangerEnum.CabinClass;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_booking_reference", columnList = "bookingReference"),
        @Index(name = "idx_booking_pnr", columnList = "pnr"),
        @Index(name = "idx_booking_user", columnList = "userId"),
        @Index(name = "idx_booking_flight", columnList = "flightId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Flight ID is required")
    @Column(nullable = false)
    private Long flightId;

    @NotNull(message = "Cabin class is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CabinClass cabinClass;

    @NotBlank(message = "Booking reference is required")
    @Size(max = 30)
    @Column(nullable = false, unique = true, length = 30)
    private String bookingReference;

    @Column(unique = true, length = 20)
    private String pnr;

    @NotNull(message = "User ID is required")
    @Column(nullable = false)
    private Long userId;

    @Builder.Default
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookingPassenger> passengers = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PaymentInfo paymentInfo;

    @NotNull(message = "Total passengers is required")
    @Min(value = 1, message = "At least one passenger is required")
    @Column(nullable = false)
    private Integer totalPassengers;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total amount must be greater than or equal to zero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @NotNull(message = "Booking status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus bookingStatus;

    @NotNull(message = "Payment status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @NotNull(message = "Booking date is required")
    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @NotNull(message = "Travel date is required")
    @Column(nullable = false)
    private LocalDate travelDate;

    @NotNull(message = "Currency is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CurrencyCode currency;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Contact email is required")
    @Column(nullable = false, length = 100)
    private String contactEmail;

    @NotBlank(message = "Contact phone is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must contain 10 to 15 digits")
    @Column(nullable = false, length = 15)
    private String contactPhone;

    @Column(length = 500)
    private String specialRequest;

    @Column(length = 500)
    private String cancellationReason;

    private LocalDateTime cancelledAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Version
    private Long version;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.bookingDate == null) {
            this.bookingDate = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}