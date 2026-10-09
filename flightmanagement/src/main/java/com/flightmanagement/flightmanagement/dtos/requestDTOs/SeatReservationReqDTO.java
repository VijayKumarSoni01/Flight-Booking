package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import java.util.List;

import com.flightmanagement.flightmanagement.enums.CabinClass;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatReservationReqDTO {

    private Long flightId;

    private CabinClass cabinClass;

    private Integer seatCount;

    private List<String> seatNumbers;

    private String bookingReference;
}