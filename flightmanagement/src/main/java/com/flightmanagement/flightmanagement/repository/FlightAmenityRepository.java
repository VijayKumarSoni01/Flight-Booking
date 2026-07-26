package com.flightmanagement.flightmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightmanagement.flightmanagement.entity.FlightAmenity;

public interface FlightAmenityRepository extends JpaRepository<FlightAmenity, Long> {

    Optional<FlightAmenity> findByFlightId(Long flightId);

    boolean existsByFlightId(Long flightId);

    void deleteByFlightId(Long flightId);

}