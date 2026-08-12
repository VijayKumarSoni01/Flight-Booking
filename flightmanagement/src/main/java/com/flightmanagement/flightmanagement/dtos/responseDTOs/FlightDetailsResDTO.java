package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightDetailsResDTO {

    private Long id;

    private String flightNumber;


    private String airlineName;

    private String airlineCode;


    private String aircraftModel;

    private String aircraftRegistration;


    private String originAirportName;

    private String originAirportCode;


    private String destinationAirportName;

    private String destinationAirportCode;


    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;


    private Integer durationMinutes;


    private String status;


    private List<FlightFareResDTO> fares;


    private List<BaggagePolicyResDTO> baggagePolicies;

}