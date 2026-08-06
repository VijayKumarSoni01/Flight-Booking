package com.project.payment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.payment.entity.Payment;
import com.project.payment.enums.CurrencyCode;
import com.project.payment.enums.PaymentGateway;
import com.project.payment.enums.PaymentMethod;
import com.project.payment.enums.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Booking

    Optional<Payment> findTopByBookingReferenceOrderByAttemptNumberDesc(
            String bookingReference);

    List<Payment> findByBookingId(Long bookingId);

    List<Payment> findByBookingIdOrderByAttemptNumberAsc(Long bookingId);

    Optional<Payment> findTopByBookingIdOrderByAttemptNumberDesc(Long bookingId);

    // User

    Page<Payment> findByUserId( Long userId, Pageable pageable);

    // Payment Status

    Page<Payment> findByPaymentStatus(
        PaymentStatus paymentStatus,
        Pageable pageable);

    boolean existsByBookingIdAndPaymentStatus(
            Long bookingId,
            PaymentStatus paymentStatus);

    // Gateway

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);

    Optional<Payment> findByGatewayPaymentId(String gatewayPaymentId);

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);

    // Reports

    Page<Payment> findByPaymentGateway(
        PaymentGateway paymentGateway,
        Pageable pageable);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    List<Payment> findByCurrency(CurrencyCode currency);

    List<Payment> findByCreatedAtBetween(
            LocalDateTime startDate,
            LocalDateTime endDate);

    // Statistics

    long countByBookingId(Long bookingId);

    Optional<Payment> findByBookingReferenceAndPaymentStatus(
        String bookingReference,
        PaymentStatus paymentStatus);

}