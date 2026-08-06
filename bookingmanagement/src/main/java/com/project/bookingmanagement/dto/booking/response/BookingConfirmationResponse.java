package com.project.bookingmanagement.dto.booking.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.bookingmanagement.enums.bookingEnum.BookingStatus;
import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingConfirmationResponse {

    private Long bookingId;

    private String bookingReference;

    private String pnr;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;

    private BigDecimal totalFare;

    private LocalDateTime bookingDate;

    private String message;
}