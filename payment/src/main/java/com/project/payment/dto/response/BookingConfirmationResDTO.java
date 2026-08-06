package com.project.payment.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.payment.enums.BookingStatus;
import com.project.payment.enums.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingConfirmationResDTO {

    private Long bookingId;
    private String bookingReference;
    private String pnr;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private BigDecimal totalFare;
    private LocalDateTime bookingDate;
    private String message;
}