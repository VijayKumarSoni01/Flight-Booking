package com.project.bookingmanagement.dto.external.flight;

import java.math.BigDecimal;

import com.project.bookingmanagement.enums.bookingEnum.CurrencyCode;

import lombok.Data;

@Data
public class FlightFareResponse {

    private Long flightId;

    private BigDecimal adultFare;

    private BigDecimal childFare;

    private BigDecimal infantFare;

    private CurrencyCode currency;
}