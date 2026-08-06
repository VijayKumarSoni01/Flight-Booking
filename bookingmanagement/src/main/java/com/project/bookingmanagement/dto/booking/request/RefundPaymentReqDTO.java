package com.project.bookingmanagement.dto.booking.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RefundPaymentReqDTO {

    private Long bookingId;

    private BigDecimal refundAmount;

    private String reason;
}
