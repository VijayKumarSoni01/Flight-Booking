package com.project.bookingmanagement.dto.booking.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.project.bookingmanagement.dto.passenger.response.PassengerResponse;
import com.project.bookingmanagement.enums.bookingEnum.BookingStatus;
import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;

import lombok.Data;

@Data
public class BookingDetailsResponse {

    private Long bookingId;

    private String bookingReference;

    private String pnr;

    private Long userId;

    private Long flightId;

    private String flightNumber;

    private String airlineName;

    private String sourceAirport;

    private String destinationAirport;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private LocalDate travelDate;

    private Integer totalPassengers;

    private BigDecimal baseFare;

    private BigDecimal taxAmount;

    private BigDecimal discountAmount;

    private BigDecimal totalFare;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private String contactEmail;

    private String contactPhone;

    private String specialRequest;

    private List<PassengerResponse> passengers;

    private LocalDateTime bookingDate;

    private LocalDateTime lastModifiedDate;
}
