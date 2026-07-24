package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import java.time.LocalDateTime;

import com.flightmanagement.flightmanagement.enums.FlightType;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FlightReqDTO {
    @NotBlank(message = "Flight number is required")
    @Size(min = 2, max = 10, message = "Flight number must be between 2 and 10 characters")
    private String flightNumber;

    @NotNull(message = "Origin airport ID is required")
    private Long originAirportId;

    @NotNull(message = "Destination airport ID is required")
    private Long destinationAirportId;

    @NotNull(message = "Aircraft ID is required")
    private Long aircraftId;

    @NotNull(message = "Flight type is required")
    private FlightType flightType;

    @NotNull(message = "Departure time is required")
    @Future(message = "Departure time must be in the future")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    @Future(message = "Arrival time must be in the future")
    private LocalDateTime arrivalTime;

    @Size(max = 20, message = "Departure terminal cannot exceed 20 characters")
    private String departureTerminal;

    @Size(max = 20, message = "Arrival terminal cannot exceed 20 characters")
    private String arrivalTerminal;
}
