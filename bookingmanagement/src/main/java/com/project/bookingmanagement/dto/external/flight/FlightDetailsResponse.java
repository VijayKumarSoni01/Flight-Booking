package com.project.bookingmanagement.dto.external.flight;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.bookingmanagement.enums.bookingEnum.FlightStatus;
import com.project.bookingmanagement.enums.bookingPassangerEnum.CabinClass;

import lombok.Data;

@Data
public class FlightDetailsResponse {

    private Long flightId;

    private String flightNumber;

    private String airlineName;

    private String aircraftName;

    private String sourceAirport;

    private String destinationAirport;

    private String departureTerminal;

    private String arrivalTerminal;

    private String departureGate;

    private String arrivalGate;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private Integer durationMinutes;

    private FlightStatus flightStatus;

    private CabinClass cabinClass;

    private BigDecimal fare;

    private Integer availableSeats;
}
