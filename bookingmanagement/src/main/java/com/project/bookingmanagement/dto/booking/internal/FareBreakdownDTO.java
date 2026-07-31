package com.project.bookingmanagement.dto.booking.internal;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FareBreakdownDTO {

    private BigDecimal baseFare;

    private BigDecimal taxes;

    private BigDecimal convenienceFee;

    private BigDecimal discount;

    private BigDecimal couponDiscount;

    private BigDecimal insuranceFee;

    private BigDecimal totalFare;
}