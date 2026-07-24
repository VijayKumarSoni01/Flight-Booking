package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightAmenityResDTO {
    private Long id;

    private Long flightId;

    private String flightNumber;

    private Boolean mealIncluded;

    private Boolean wifiAvailable;

    private Boolean usbCharging;

    private Boolean entertainmentSystem;
}
