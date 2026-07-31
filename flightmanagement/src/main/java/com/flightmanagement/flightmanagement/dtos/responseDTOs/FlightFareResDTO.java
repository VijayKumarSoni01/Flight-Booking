package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import java.math.BigDecimal;

import com.flightmanagement.flightmanagement.enums.CabinClass;
import com.flightmanagement.flightmanagement.enums.CurrencyCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightFareResDTO {

    private Long id;

    private Long flightId;

    private String flightNumber;

    private CabinClass cabinClass;

    private BigDecimal adultFare;

    private BigDecimal childFare;

    private BigDecimal infantFare;

    private CurrencyCode currency;

    private Boolean includesTax;
}