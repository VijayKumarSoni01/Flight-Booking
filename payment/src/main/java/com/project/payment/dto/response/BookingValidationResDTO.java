package com.project.payment.dto.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BookingValidationResDTO {

    private Long bookingId;

    private String bookingReference;

    private Long userId;

    private BigDecimal totalAmount;

    private String currency;

    private String bookingStatus;

    private String paymentStatus;

}
