package com.project.bookingmanagement.dto.booking.internal;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TaxCalculationDTO {

    private BigDecimal gst;

    private BigDecimal airportTax;

    private BigDecimal serviceTax;

    private BigDecimal fuelSurcharge;

    private BigDecimal totalTax;
}
