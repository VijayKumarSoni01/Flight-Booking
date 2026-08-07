package com.project.notificationmanagement.dto.response;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookingResponse {


    private Long bookingId;


    private Long userId;


    private String bookingReference;


    private String pnr;


    private String airlineName;


    private String flightNumber;


    private String sourceAirport;


    private String destinationAirport;


    private LocalDateTime departureTime;


    private LocalDateTime arrivalTime;


    private String seatNumbers;


    private Integer passengerCount;


    private String bookingStatus;

}