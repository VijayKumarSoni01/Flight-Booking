package com.project.bookingmanagement.dto.booking.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.project.bookingmanagement.enums.bookingEnum.BookingStatus;
import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;

import lombok.Data;

@Data
public class BookingResponse {

    private Long bookingId;

    private String bookingReference;

    private String pnr;

    private Long userId;

    private Long flightId;

    private String flightNumber;

    private String airlineName;

    private String sourceAirport;

    private String destinationAirport;

    private LocalDate travelDate;

    private Integer totalPassengers;

    private BigDecimal totalFare;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private LocalDateTime bookingDate;
}
