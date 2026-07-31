package com.project.bookingmanagement.dto.booking.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeFlightRequest {

    @NotNull(message = "New flight id is required")
    private Long newFlightId;

    @NotNull(message = "New travel date is required")
    @Future(message = "Travel date must be in the future")
    private LocalDate newTravelDate;
}
