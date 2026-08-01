package com.flightmanagement.flightmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flightmanagement.flightmanagement.entity.Seat;
import com.flightmanagement.flightmanagement.enums.CabinClass;
import com.flightmanagement.flightmanagement.enums.SeatStatus;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByFlightIdAndCabinClass(
            Long flightId,
            CabinClass cabinClass);

    List<Seat> findByFlightIdAndCabinClassAndSeatStatus(
            Long flightId,
            CabinClass cabinClass,
            SeatStatus seatStatus);

    Optional<Seat> findByFlightIdAndSeatNumber(
            Long flightId,
            String seatNumber);

    long countByFlightIdAndCabinClassAndSeatStatus(
            Long flightId,
            CabinClass cabinClass,
            SeatStatus seatStatus);

    List<Seat> findByFlightId(Long flightId);

    List<Seat> findByBookingReference(String bookingReference);

    @Query("""
            SELECT s
            FROM Seat s
            WHERE s.flight.id = :flightId
            AND s.cabinClass = :cabinClass
            AND s.seatStatus = :status
            ORDER BY s.seatIndex
            """)
    List<Seat> findAvailableSeats(
            @Param("flightId") Long flightId,
            @Param("cabinClass") CabinClass cabinClass,
            @Param("status") SeatStatus status,
            Pageable pageable);

    List<Seat> findByFlightIdAndCabinClassAndSeatStatusOrderBySeatIndexAsc(
            Long flightId,
            CabinClass cabinClass,
            SeatStatus seatStatus);

            boolean existsByFlightId(Long flightId);
}