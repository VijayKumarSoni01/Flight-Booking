package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import java.math.BigDecimal;

import com.flightmanagement.flightmanagement.enums.CurrencyCode;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlightFareUpdateReqDTO {

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 8, fraction = 2)
    private BigDecimal price;

    @NotNull(message = "Currency is required")
    private CurrencyCode currency;

    private Boolean includesTax = true;
}