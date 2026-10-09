package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import com.flightmanagement.flightmanagement.enums.CabinClass;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatReqDTO {

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "Cabin class is required")
    private CabinClass cabinClass;
}