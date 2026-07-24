package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import java.time.LocalDateTime;

import com.flightmanagement.flightmanagement.enums.FlightStatus;
import com.flightmanagement.flightmanagement.enums.FlightType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightResDTO {
    private Long id;

    private String flightNumber;

    private Long airlineId;
    private String airlineName;
    private String airlineCode;

    private Long aircraftId;
    private String aircraftRegistration;
    private String aircraftModel;

    private Long originAirportId;
    private String originAirportName;
    private String originAirportCode;

    private Long destinationAirportId;
    private String destinationAirportName;
    private String destinationAirportCode;

    private FlightType flightType;

    private FlightStatus status;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private Integer durationMinutes;

    private String departureTerminal;

    private String arrivalTerminal;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
