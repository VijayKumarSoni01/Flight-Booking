package com.flightmanagement.flightmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

        @Query("""
                        SELECT DISTINCT f FROM Flight f

                        JOIN FETCH f.airline

                        JOIN FETCH f.aircraft

                        JOIN FETCH f.originAirport

                        JOIN FETCH f.destinationAirport

                        LEFT JOIN FETCH f.flightFares

                        WHERE

                        (
                        LOWER(f.originAirport.iataCode)=LOWER(:source)
                        OR
                        LOWER(f.originAirport.city)=LOWER(:source)
                        )

                        AND

                        (
                        LOWER(f.destinationAirport.iataCode)=LOWER(:destination)
                        OR
                        LOWER(f.destinationAirport.city)=LOWER(:destination)
                        )

                        AND

                        f.departureTime >= :startDate

                        AND

                        f.departureTime < :endDate

                        """)
        List<Flight> searchFlights(
                        String source,
                        String destination,
                        LocalDateTime startDate,
                        LocalDateTime endDate);

        @Query("""
                        SELECT DISTINCT f FROM Flight f

                        JOIN FETCH f.airline

                        JOIN FETCH f.aircraft

                        JOIN FETCH f.originAirport

                        JOIN FETCH f.destinationAirport

                        LEFT JOIN FETCH f.flightFares

                        WHERE f.id = :flightId

                        """)
        Optional<Flight> findFlightDetailsById(Long flightId);

}