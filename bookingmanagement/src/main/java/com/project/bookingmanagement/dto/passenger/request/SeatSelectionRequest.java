package com.project.bookingmanagement.dto.passenger.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SeatSelectionRequest {

    @NotBlank(message = "Seat number is required")
    @Pattern(
        regexp = "^[1-9][0-9]?[A-F]$",
        message = "Seat number must be in the format like 12A, 5C, or 24F"
    )
    private String seatNumber;
}