package com.project.notificationmanagement.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.notificationmanagement.enums.CabinClass;
import com.project.notificationmanagement.enums.CurrencyCode;
import com.project.notificationmanagement.enums.PaymentMethod;
import com.project.notificationmanagement.enums.PaymentStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class BookingConfirmationEmailRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    @Email(message = "Invalid recipient email")
    @NotBlank(message = "Recipient email is required")
    private String recipientEmail;

    @NotBlank(message = "Booking reference is required")
    private String bookingReference;

    @NotBlank(message = "PNR is required")
    private String pnr;

    @NotBlank(message = "Airline name is required")
    private String airlineName;

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Source airport is required")
    private String sourceAirport;

    @NotBlank(message = "Destination airport is required")
    private String destinationAirport;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivalTime;

    @NotBlank(message = "Seat numbers are required")
    private String seatNumbers;

    @NotNull(message = "Cabin class is required")
    private CabinClass cabinClass;

    @NotNull(message = "Passenger count is required")
    @Min(value = 1, message = "Passenger count must be at least 1")
    private Integer passengerCount;

    @NotNull(message = "Total fare is required")
    @Positive(message = "Total fare must be greater than zero")
    private BigDecimal totalFare;

    @NotNull(message = "Currency is required")
    private CurrencyCode currency;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    @NotNull(message = "Booking time is required")
    private LocalDateTime bookedAt;
}