package com.project.bookingmanagement.dto.external.flight;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResDTO {

    private Long id;

    private String seatNumber;

    private String cabinClass;

    private String seatStatus;

    private String bookingReference;

    private LocalDateTime reservedAt;

    private Long flightId;

    private String flightNumber;
}