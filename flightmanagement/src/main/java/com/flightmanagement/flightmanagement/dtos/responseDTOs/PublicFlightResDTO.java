package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.flightmanagement.flightmanagement.enums.CurrencyCode;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PublicFlightResDTO {


    private Long id;

    private String flightNumber;

    private String airlineName;

    private String airlineCode;

    private String originAirportName;

    private String originAirportCode;

    private String destinationAirportName;

    private String destinationAirportCode;

    private BigDecimal economyPrice;

    private BigDecimal premiumEconomyPrice;

    private BigDecimal businessPrice;

    private BigDecimal firstPrice;

    private CurrencyCode currency;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private String departureTerminal;

    private String arrivalTerminal;

    private Integer durationMinutes;

    private List<BaggagePolicyResDTO> baggagePolicies;

}