package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import java.math.BigDecimal;

import com.flightmanagement.flightmanagement.enums.CabinClass;
import com.flightmanagement.flightmanagement.enums.CurrencyCode;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlightFareReqDTO {
    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotNull(message = "Cabin class is required")
    private CabinClass cabinClass;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have up to 8 integer digits and 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "Currency is required")
    private CurrencyCode currency;

    private Boolean includesTax = true;
}
