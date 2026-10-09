package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import java.time.LocalTime;

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
public class FlightScheduleRequest {

    private Long flightId;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private FrequencyType frequency;
}
