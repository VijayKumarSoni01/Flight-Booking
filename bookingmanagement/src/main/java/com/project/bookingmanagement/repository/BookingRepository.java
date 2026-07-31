package com.project.bookingmanagement.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bookingmanagement.entity.Booking;
import com.project.bookingmanagement.enums.bookingEnum.BookingStatus;
import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Booking Reference & PNR
    // ==========================

    Optional<Booking> findByBookingReference(String bookingReference);

    Optional<Booking> findByPnr(String pnr);

    boolean existsByBookingReference(String bookingReference);

    boolean existsByPnr(String pnr);

    // User Booking Queries
    // ==========================

    List<Booking> findByUserId(Long userId);

    List<Booking> findByUserIdOrderByBookingDateDesc(Long userId);

    List<Booking> findByUserIdAndBookingStatus(
            Long userId,
            BookingStatus bookingStatus);

    List<Booking> findByUserIdAndPaymentStatus(
            Long userId,
            PaymentStatus paymentStatus);

    // Flight Queries
    // ==========================

    List<Booking> findByFlightId(Long flightId);

    List<Booking> findByFlightIdAndBookingStatus(
            Long flightId,
            BookingStatus bookingStatus);

    // Booking Status
    // ==========================

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);

    long countByBookingStatus(BookingStatus bookingStatus);

    // Payment Status
    // ==========================

    List<Booking> findByPaymentStatus(PaymentStatus paymentStatus);

    long countByPaymentStatus(PaymentStatus paymentStatus);

    // Travel Date
    // ==========================

    List<Booking> findByTravelDate(LocalDate travelDate);

    List<Booking> findByTravelDateBetween(
            LocalDate startDate,
            LocalDate endDate);

    // Booking Date
    // ==========================

    List<Booking> findByBookingDateBetween(
            LocalDateTime start,
            LocalDateTime end);

    // Combined Queries
    // ==========================

    List<Booking> findByTravelDateAndBookingStatus(
            LocalDate travelDate,
            BookingStatus bookingStatus);

    List<Booking> findByTravelDateAndPaymentStatus(
            LocalDate travelDate,
            PaymentStatus paymentStatus);

    List<Booking> findByBookingStatusAndPaymentStatus(
            BookingStatus bookingStatus,
            PaymentStatus paymentStatus);

    // Counts
    // ==========================

    long countByUserId(Long userId);

    long countByFlightId(Long flightId);

    long countByTravelDate(LocalDate travelDate);

    // Existence
    // ==========================

    boolean existsByUserIdAndFlightId(
            Long userId,
            Long flightId);

    void deleteByBookingStatusAndCancelledAtBefore(BookingStatus cancelled, LocalDateTime cutoff);

    List<Booking> findByBookingStatusAndExpiresAtBefore(
        BookingStatus bookingStatus,
        LocalDateTime time);
}