package com.flightmanagement.flightmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightmanagement.flightmanagement.entity.Flight;
import com.flightmanagement.flightmanagement.enums.FlightStatus;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    boolean existsByFlightNumberAndDepartureTime(
            String flightNumber,
            LocalDateTime departureTime);

    boolean existsByFlightNumberAndDepartureTimeAndIdNot(
            String flightNumber,
            LocalDateTime departureTime,
            Long id);

    List<Flight> findByOriginAirportIdAndDestinationAirportIdAndDepartureTimeBetween(
            Long originAirportId,
            Long destinationAirportId,
            LocalDateTime start,
            LocalDateTime end);

    List<Flight> findByStatusAndDepartureTimeBetween(
            FlightStatus status,
            LocalDateTime start,
            LocalDateTime end);

    List<Flight> findByStatusAndArrivalTimeBefore(
            FlightStatus status,
            LocalDateTime arrivalTime);
}