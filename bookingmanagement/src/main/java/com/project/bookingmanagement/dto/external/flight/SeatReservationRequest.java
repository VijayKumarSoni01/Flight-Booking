package com.project.bookingmanagement.dto.external.flight;


import com.project.bookingmanagement.enums.bookingPassangerEnum.CabinClass;

import lombok.Data;

@Data
public class SeatReservationRequest {

    private CabinClass cabinClass;

    private Integer seatCount;

    private String bookingReference;
}