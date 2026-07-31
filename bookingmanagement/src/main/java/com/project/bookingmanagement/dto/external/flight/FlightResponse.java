package com.project.bookingmanagement.dto.external.flight;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.bookingmanagement.enums.bookingEnum.CurrencyCode;
import com.project.bookingmanagement.enums.bookingEnum.FlightStatus;
import com.project.bookingmanagement.enums.bookingPassangerEnum.CabinClass;

import lombok.Data;

@Data
public class FlightResponse {

    private Long flightId;

    private String flightNumber;

    private String airlineName;

    private String sourceAirport;

    private String destinationAirport;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private FlightStatus flightStatus;

    private CabinClass cabinClass;

    private BigDecimal fare;

    private CurrencyCode currency;

    private Integer availableSeats;
}