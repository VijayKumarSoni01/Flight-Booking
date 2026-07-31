package com.project.bookingmanagement.dto.booking.internal;

import java.math.BigDecimal;

import com.project.bookingmanagement.enums.bookingEnum.CurrencyCode;

import lombok.Data;

@Data
public class BookingPriceDTO {

    private BigDecimal adultFare;

    private BigDecimal childFare;

    private BigDecimal infantFare;

    private BigDecimal totalFare;

    private CurrencyCode currency;
}