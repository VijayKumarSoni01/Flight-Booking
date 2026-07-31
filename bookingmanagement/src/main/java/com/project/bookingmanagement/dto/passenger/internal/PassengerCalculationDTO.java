package com.project.bookingmanagement.dto.passenger.internal;

import java.math.BigDecimal;

import com.project.bookingmanagement.enums.bookingPassangerEnum.PassengerType;

import lombok.Data;

@Data
public class PassengerCalculationDTO {

    private PassengerType passengerType;

    private BigDecimal fare;

    private BigDecimal tax;

    private BigDecimal discount;

    private BigDecimal totalFare;
}
