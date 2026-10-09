package com.flightmanagement.flightmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightmanagement.flightmanagement.entity.Seat;
import com.flightmanagement.flightmanagement.enums.CabinClass;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByFlightId(Long flightId);

    List<Seat> findByFlightIdAndCabinClass(
            Long flightId,
            CabinClass cabinClass);



    Optional<Seat> findByFlightIdAndSeatNumber(
            Long flightId,
            String seatNumber);


    boolean existsByFlightId(Long flightId);
}