package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlightAmenityReqDTO {
    @NotNull(message = "Flight ID is required")
    private Long flightId;

    private Boolean mealIncluded = false;

    private Boolean wifiAvailable = false;

    private Boolean usbCharging = false;

    private Boolean entertainmentSystem = false;
}
