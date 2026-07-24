package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import java.time.LocalDateTime;

import com.flightmanagement.flightmanagement.enums.FlightStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FlightStatusInfoReqDTO {
    
    @NotNull(message = "Flight ID is required")
    private Long flightId;

    private LocalDateTime estimatedDeparture;

    private LocalDateTime estimatedArrival;

    private LocalDateTime actualDeparture;

    private LocalDateTime actualArrival;

    private Integer delayMinutes = 0;

    @Size(max = 10, message = "Departure gate cannot exceed 10 characters")
    private String departureGate;

    @Size(max = 10, message = "Arrival gate cannot exceed 10 characters")
    private String arrivalGate;

    private FlightStatus status = FlightStatus.SCHEDULED;
}
