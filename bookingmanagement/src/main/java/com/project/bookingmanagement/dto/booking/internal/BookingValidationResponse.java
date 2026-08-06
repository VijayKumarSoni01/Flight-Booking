package com.project.bookingmanagement.dto.booking.internal;

import java.math.BigDecimal;

import com.project.bookingmanagement.enums.bookingEnum.BookingStatus;
import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;

import lombok.Data;

@Data
public class BookingValidationResponse {

    private Long bookingId;

    private String bookingReference;

    private Long userId;

    private BigDecimal totalAmount;

    private String currency;

    private BookingStatus bookingStatus;

    private PaymentStatus paymentStatus;
}
