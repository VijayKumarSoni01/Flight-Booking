package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import java.math.BigDecimal;

import com.flightmanagement.flightmanagement.enums.CurrencyCode;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlightFareUpdateReqDTO {

    @NotNull(message = "Adult fare is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Adult fare must be greater than 0")
    @Digits(integer = 8, fraction = 2,
            message = "Adult fare must have up to 8 integer digits and 2 decimal places")
    private BigDecimal adultFare;

    @NotNull(message = "Child fare is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Child fare must be greater than 0")
    @Digits(integer = 8, fraction = 2,
            message = "Child fare must have up to 8 integer digits and 2 decimal places")
    private BigDecimal childFare;

    @NotNull(message = "Infant fare is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Infant fare must be greater than 0")
    @Digits(integer = 8, fraction = 2,
            message = "Infant fare must have up to 8 integer digits and 2 decimal places")
    private BigDecimal infantFare;

    @NotNull(message = "Currency is required")
    private CurrencyCode currency;

    private Boolean includesTax = true;
}