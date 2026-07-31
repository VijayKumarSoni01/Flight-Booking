package com.project.bookingmanagement.exception;

public class BookingNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BookingNotFoundException(String message) {
        super(message);
    }

    public BookingNotFoundException(Long bookingId) {
        super("Booking not found with ID: " + bookingId);
    }

    public static BookingNotFoundException byReference(String bookingReference) {
        return new BookingNotFoundException(
                "Booking not found with reference: " + bookingReference);
    }
}