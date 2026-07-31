package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import com.flightmanagement.flightmanagement.enums.CabinClass;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatAvailabilityResDTO {

    private Long flightId;

    private CabinClass cabinClass;

    private Long totalSeats;

    private Long availableSeats;

    private Long bookedSeats;

    private Long heldSeats;

    private Long blockedSeats;

    private Boolean available;
}