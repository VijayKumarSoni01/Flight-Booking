package com.project.bookingmanagement.dto.booking.internal;

import java.math.BigDecimal;
import java.util.List;

import com.project.bookingmanagement.dto.passenger.internal.PassengerCalculationDTO;

import lombok.Data;

@Data
public class BookingCalculationDTO {

    private Integer totalPassengers;

    private BigDecimal totalFare;

    private FareBreakdownDTO fareBreakdown;

    private List<PassengerCalculationDTO> passengers;
}