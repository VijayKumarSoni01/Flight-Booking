package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatReservationReqDTO {

    @NotNull
    private Long flightId;

    @NotEmpty
    private List<String> seatNumbers;

    private String bookingReference;
}