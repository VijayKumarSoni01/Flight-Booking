package com.project.payment.exception;

public class PaymentAlreadyCompletedException extends RuntimeException {

    public PaymentAlreadyCompletedException(String bookingReference) {
        super("Payment already completed successfully for booking reference: " + bookingReference);
    }
}
