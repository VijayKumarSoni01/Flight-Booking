package com.project.bookingmanagement.dto.external.flight;


import com.project.bookingmanagement.enums.bookingEnum.FlightStatus;
import com.project.bookingmanagement.enums.bookingPassangerEnum.CabinClass;

import lombok.Data;

@Data
public class FlightAvailabilityResponse {

    private Long flightId;

    private CabinClass cabinClass;

    private boolean seatAvailable;

    private Integer availableSeats;

    private Integer totalSeats;

    private FlightStatus flightStatus;
}
