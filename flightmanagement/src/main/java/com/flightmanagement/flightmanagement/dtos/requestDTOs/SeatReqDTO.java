package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import com.flightmanagement.flightmanagement.enums.CabinClass;
import com.flightmanagement.flightmanagement.enums.SeatStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatReqDTO {

    @NotNull
    private Long flightId;

    @NotBlank
    private String seatNumber;

    @NotNull
    private CabinClass cabinClass;

    @NotNull
    private SeatStatus seatStatus;

    private String bookingReference;
}