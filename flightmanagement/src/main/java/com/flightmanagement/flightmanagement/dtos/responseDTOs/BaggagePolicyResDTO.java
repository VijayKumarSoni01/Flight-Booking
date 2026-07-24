package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import java.math.BigDecimal;

import com.flightmanagement.flightmanagement.enums.CabinClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaggagePolicyResDTO {

    private Long id;

    private Long flightId;

    private String flightNumber;

    private CabinClass cabinClass;

    private Integer cabinBaggageKg;

    private Integer checkinBaggageKg;

    private BigDecimal extraBaggagePricePerKg;
}