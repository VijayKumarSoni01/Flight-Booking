package com.flightmanagement.flightmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightmanagement.flightmanagement.entity.FlightFare;
import com.flightmanagement.flightmanagement.enums.CabinClass;

public interface FlightFareRepository extends JpaRepository<FlightFare, Long> {

    Optional<FlightFare> findByFlightIdAndCabinClass(
            Long flightId,
            CabinClass cabinClass);

    List<FlightFare> findByFlightId(Long flightId);

    boolean existsByFlightIdAndCabinClass(
            Long flightId,
            CabinClass cabinClass);

    boolean existsByFlightIdAndCabinClassAndIdNot(
            Long flightId,
            CabinClass cabinClass,
            Long id);
}