package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import com.flightmanagement.flightmanagement.enums.CabinClass;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoSeatReservationReqDTO {

    @NotNull
    private CabinClass cabinClass;

    @NotNull
    private Integer seatCount;

    @NotNull
    private String bookingReference;
}