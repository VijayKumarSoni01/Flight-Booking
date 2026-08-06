package com.project.bookingmanagement.exception;

public class BookingCancellationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BookingCancellationException(String message) {
        super(message);
    }

    public BookingCancellationException(String message, Throwable cause) {
        super(message, cause);
    }
}