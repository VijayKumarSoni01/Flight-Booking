package com.project.bookingmanagement.exception;

public class CouponNotValidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CouponNotValidException(String message) {
        super(message);
    }

    public CouponNotValidException(String couponCode, String reason) {
        super("Coupon '" + couponCode + "' is invalid. Reason: " + reason);
    }
}