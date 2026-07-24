package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import java.time.LocalDateTime;

import com.flightmanagement.flightmanagement.enums.FlightStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightStatusInfoResDTO {

    private Long id;

    private Long flightId;

    private String flightNumber;

    private LocalDateTime estimatedDeparture;

    private LocalDateTime estimatedArrival;

    private LocalDateTime actualDeparture;

    private LocalDateTime actualArrival;

    private Integer delayMinutes;

    private String departureGate;

    private String arrivalGate;

    private LocalDateTime lastApiSync;

    private FlightStatus status;
}