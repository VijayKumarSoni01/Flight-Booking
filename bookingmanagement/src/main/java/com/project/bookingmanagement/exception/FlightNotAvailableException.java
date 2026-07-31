package com.project.bookingmanagement.exception;

public class FlightNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FlightNotAvailableException(String message) {
        super(message);
    }

    public FlightNotAvailableException(Long flightId) {
        super("Flight is not available. Flight ID: " + flightId);
    }
}