package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import java.math.BigDecimal;

import com.flightmanagement.flightmanagement.enums.CabinClass;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaggagePolicyReqDTO {

        @NotNull(message = "Flight ID is required")
        private Long flightId;

        @NotNull(message = "Cabin class is required")
        private CabinClass cabinClass;

        @NotNull(message = "Cabin baggage weight is required")
        @Min(value = 0, message = "Cabin baggage weight cannot be negative")
        private Integer cabinBaggageKg;

        @NotNull(message = "Check-in baggage weight is required")
        @Min(value = 0, message = "Check-in baggage weight cannot be negative")
        private Integer checkinBaggageKg;

        @DecimalMin(value = "0.0", inclusive = true, message = "Extra baggage price cannot be negative")
        @Digits(integer = 8, fraction = 2, message = "Price must have up to 8 integer digits and 2 decimal places")
        private BigDecimal extraBaggagePricePerKg;
}