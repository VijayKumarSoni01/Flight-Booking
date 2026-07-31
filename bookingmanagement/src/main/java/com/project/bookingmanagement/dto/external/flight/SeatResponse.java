package com.project.bookingmanagement.dto.external.flight;


import com.project.bookingmanagement.enums.bookingPassangerEnum.CabinClass;

import lombok.Data;

@Data
public class SeatResponse {

    private String seatNumber;

    private CabinClass cabinClass;

    private boolean emergencyExit;

    private boolean extraLegroom;
}