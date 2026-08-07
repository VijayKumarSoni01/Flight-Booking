package com.project.notificationmanagement.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.notificationmanagement.enums.RefundStatus;

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
public class BookingCancellationEmailRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid recipient email")
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

    @NotNull(message = "Cancellation time is required")
    private LocalDateTime cancelledAt;

    @NotBlank(message = "Cancellation reason is required")
    private String cancellationReason;

    @NotNull(message = "Refund amount is required")
    @Positive(message = "Refund amount must be greater than zero")
    private BigDecimal refundAmount;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Refund status is required")
    private RefundStatus refundStatus;

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    @NotNull(message = "Passenger count is required")
    @Min(value = 1, message = "Passenger count must be at least 1")
    private Integer passengerCount;
}