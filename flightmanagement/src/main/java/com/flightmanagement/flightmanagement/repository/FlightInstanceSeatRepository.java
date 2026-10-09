package com.flightmanagement.flightmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flightmanagement.flightmanagement.entity.FlightInstanceSeat;
import com.flightmanagement.flightmanagement.enums.CabinClass;
import com.flightmanagement.flightmanagement.enums.SeatStatus;

import jakarta.persistence.LockModeType;

public interface FlightInstanceSeatRepository
                extends JpaRepository<FlightInstanceSeat, Long> {

        List<FlightInstanceSeat> findByFlightInstanceId(
                        Long flightInstanceId);

        List<FlightInstanceSeat> findByFlightInstanceIdAndSeat_CabinClass(
                        Long flightInstanceId,
                        CabinClass cabinClass);

        Optional<FlightInstanceSeat> findByFlightInstanceIdAndSeat_SeatNumber(
                        Long flightInstanceId,
                        String seatNumber);

        List<FlightInstanceSeat> findByFlightInstanceIdAndSeat_CabinClassAndSeatStatus(
                        Long flightInstanceId,
                        CabinClass cabinClass,
                        SeatStatus seatStatus);

        long countByFlightInstanceIdAndSeat_CabinClassAndSeatStatus(
                        Long flightInstanceId,
                        CabinClass cabinClass,
                        SeatStatus seatStatus);

        List<FlightInstanceSeat> findByBookingReference(String bookingReference);

        @Query("""
                        SELECT fis
                        FROM FlightInstanceSeat fis
                        WHERE fis.flightInstance.id = :flightInstanceId
                        AND fis.seat.cabinClass = :cabinClass
                        AND fis.seatStatus = :status
                        ORDER BY fis.seat.seatIndex
                        """)
        List<FlightInstanceSeat> findAvailableSeats(
                        @Param("flightInstanceId") Long flightInstanceId,
                        @Param("cabinClass") CabinClass cabinClass,
                        @Param("status") SeatStatus status,
                        Pageable pageable);

        List<FlightInstanceSeat> findByFlightInstanceIdAndSeat_CabinClassAndSeatStatusOrderBySeat_SeatIndexAsc(
                        Long flightInstanceId,
                        CabinClass cabinClass,
                        SeatStatus seatStatus);

        boolean existsByFlightInstanceId(Long flightInstanceId);

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
                        SELECT fis
                        FROM FlightInstanceSeat fis
                        JOIN fis.seat s
                        WHERE fis.flightInstance.id = :flightInstanceId
                        AND s.seatNumber = :seatNumber
                        """)
        Optional<FlightInstanceSeat> findByFlightInstanceIdAndSeatNumberForUpdate(
                        @Param("flightInstanceId") Long flightInstanceId,
                        @Param("seatNumber") String seatNumber);

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
                        SELECT fis
                        FROM FlightInstanceSeat fis
                        JOIN fis.seat s
                        WHERE fis.flightInstance.id = :flightInstanceId
                        AND s.cabinClass = :cabinClass
                        AND fis.seatStatus = :status
                        ORDER BY s.seatIndex
                        """)
        List<FlightInstanceSeat> findAvailableSeatsForUpdate(
                        @Param("flightInstanceId") Long flightInstanceId,
                        @Param("cabinClass") CabinClass cabinClass,
                        @Param("status") SeatStatus status,
                        Pageable pageable);
}