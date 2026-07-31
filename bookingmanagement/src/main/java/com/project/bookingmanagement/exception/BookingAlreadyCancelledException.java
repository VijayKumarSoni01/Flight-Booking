package com.project.bookingmanagement.exception;

public class BookingAlreadyCancelledException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BookingAlreadyCancelledException(String message) {
        super(message);
    }

    public BookingAlreadyCancelledException(Long bookingId) {
        super("Booking is already cancelled. Booking ID: " + bookingId);
    }
}