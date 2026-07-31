package com.project.bookingmanagement.dto.booking.internal;

import java.math.BigDecimal;

import com.project.bookingmanagement.enums.paymentInfoEnum.CouponValidationStatus;

import lombok.Data;

@Data
public class CouponValidationDTO {

    private boolean valid;

    private String couponCode;

    private BigDecimal discountAmount;

    private CouponValidationStatus status;

    private String message;
}
