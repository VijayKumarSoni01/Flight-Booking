package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import java.time.LocalDateTime;

import com.flightmanagement.flightmanagement.enums.CabinClass;
import com.flightmanagement.flightmanagement.enums.SeatStatus;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResDTO {

    private Long id;

    private String seatNumber;

    private CabinClass cabinClass;

    private SeatStatus seatStatus;

    private String bookingReference;

    private LocalDateTime reservedAt;

    private Long flightId;

    private String flightNumber;
}