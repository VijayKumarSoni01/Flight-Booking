package com.project.bookingmanagement.dto.passenger.response;

import com.project.bookingmanagement.enums.bookingPassangerEnum.CabinClass;
import com.project.bookingmanagement.enums.bookingPassangerEnum.SeatPreference;

import lombok.Data;

@Data
public class PassengerSeatResponse {

    private Long passengerId;

    private String passengerName;

    private String flightNumber;

    private CabinClass cabinClass;

    private String seatNumber;

    private SeatPreference seatPreference;

    private Boolean seatAllocated;
}
