package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import java.time.LocalTime;

import com.flightmanagement.flightmanagement.enums.FlightStatus;
import com.flightmanagement.flightmanagement.enums.FrequencyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightScheduleResponseDTO {

    private Long id;

    private Long flightId;

    private String flightNumber;

    private String airlineName;

    private String originAirport;

    private String destinationAirport;

    private String aircraft;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private FrequencyType frequency;

    private FlightStatus status;
}