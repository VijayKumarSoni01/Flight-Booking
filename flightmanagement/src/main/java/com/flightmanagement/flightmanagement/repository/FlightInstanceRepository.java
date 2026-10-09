package com.flightmanagement.flightmanagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flightmanagement.flightmanagement.entity.FlightInstance;

public interface FlightInstanceRepository
                extends JpaRepository<FlightInstance, Long> {

        @Query("""
                        SELECT DISTINCT fi
                        FROM FlightInstance fi

                        JOIN FETCH fi.schedule s

                        JOIN FETCH s.flight f

                        JOIN FETCH f.airline

                        JOIN FETCH f.originAirport

                        JOIN FETCH f.destinationAirport

                        WHERE f.originAirport.id = :originAirportId

                          AND f.destinationAirport.id = :destinationAirportId

                          AND fi.flightDate = :date

                          AND fi.status <> com.flightmanagement.flightmanagement.enums.FlightStatus.CANCELLED

                        ORDER BY fi.departureTime
                        """)
        List<FlightInstance> searchFlights(
                        @Param("originAirportId") Long originAirportId,
                        @Param("destinationAirportId") Long destinationAirportId,
                        @Param("date") LocalDate date);

        @Query("""
                        SELECT DISTINCT fi
                        FROM FlightInstance fi

                        JOIN FETCH fi.schedule s

                        JOIN FETCH s.flight f

                        JOIN FETCH f.airline

                        JOIN FETCH f.originAirport

                        JOIN FETCH f.destinationAirport

                        WHERE

                            (
                                LOWER(f.originAirport.iataCode) = LOWER(:source)

                                OR

                                LOWER(f.originAirport.city) = LOWER(:source)
                            )

                        AND

                            (
                                LOWER(f.destinationAirport.iataCode) = LOWER(:destination)

                                OR

                                LOWER(f.destinationAirport.city) = LOWER(:destination)
                            )

                        AND fi.flightDate = :date

                        AND fi.status <> com.flightmanagement.flightmanagement.enums.FlightStatus.CANCELLED

                        ORDER BY fi.departureTime
                        """)
        List<FlightInstance> searchPublicFlights(
                        @Param("source") String source,
                        @Param("destination") String destination,
                        @Param("date") LocalDate date);

        boolean existsByScheduleIdAndFlightDate(
                        Long scheduleId,
                        LocalDate flightDate);

}