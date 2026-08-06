package com.project.payment.exception;

public class PaymentRefundException extends RuntimeException {

    public PaymentRefundException(String message) {
        super(message);
    }

    public PaymentRefundException(String message, Throwable cause) {
        super(message, cause);
    }
}
