package com.project.bookingmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bookingmanagement.entity.BookingPassenger;
import com.project.bookingmanagement.enums.bookingPassangerEnum.CabinClass;
import com.project.bookingmanagement.enums.bookingPassangerEnum.Gender;
import com.project.bookingmanagement.enums.bookingPassangerEnum.PassengerType;

public interface BookingPassengerRepository extends JpaRepository<BookingPassenger, Long> {

        // Booking
        // ==========================

        List<BookingPassenger> findByBookingId(Long bookingId);

        Optional<BookingPassenger> findByIdAndBookingId(Long passengerId, Long bookingId);

        boolean existsByIdAndBookingId(Long passengerId, Long bookingId);

        long countByBookingId(Long bookingId);

        void deleteByBookingId(Long bookingId);

        // Passenger Type
        // ==========================

        List<BookingPassenger> findByPassengerType(PassengerType passengerType);

        List<BookingPassenger> findByBookingIdAndPassengerType(
                        Long bookingId,
                        PassengerType passengerType);

        // Gender
        // ==========================

        List<BookingPassenger> findByGender(Gender gender);

        List<BookingPassenger> findByBookingIdAndGender(
                        Long bookingId,
                        Gender gender);

        // Cabin Class
        // ==========================

        List<BookingPassenger> findByBooking_CabinClass(CabinClass cabinClass);

        List<BookingPassenger> findByBookingIdAndBooking_CabinClass(
                        Long bookingId,
                        CabinClass cabinClass);

        // Passport
        // ==========================

        Optional<BookingPassenger> findByPassportNumber(String passportNumber);

        boolean existsByPassportNumber(String passportNumber);

        // Seat
        // ==========================

        Optional<BookingPassenger> findBySeatNumber(String seatNumber);

        boolean existsByBookingIdAndSeatNumber(
                        Long bookingId,
                        String seatNumber);

        List<BookingPassenger> findByBookingIdAndSeatNumberIsNotNull(Long bookingId);

        // Name Search
        // ==========================

        List<BookingPassenger> findByFirstNameContainingIgnoreCase(String firstName);

        List<BookingPassenger> findByLastNameContainingIgnoreCase(String lastName);

        List<BookingPassenger> findByBookingIdOrderByLastNameAsc(Long bookingId);
}