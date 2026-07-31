package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatReservationResponse {

    private String bookingReference;

    private Integer reservedCount;

    private List<SeatResDTO> seats;
}
