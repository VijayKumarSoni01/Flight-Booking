package com.project.bookingmanagement.dto.external.flight;

import lombok.Data;

@Data
public class SeatAvailabilityResponse {

    private Long flightId;

    private String cabinClass;

    private Long totalSeats;

    private Long availableSeats;

    private Long bookedSeats;

    private Long heldSeats;

    private Long blockedSeats;

    private Boolean available;
}