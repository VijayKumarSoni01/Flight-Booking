package com.project.bookingmanagement.dto.external.flight;

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