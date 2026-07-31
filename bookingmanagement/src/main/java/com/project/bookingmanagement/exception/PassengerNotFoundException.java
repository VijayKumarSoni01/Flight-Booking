package com.project.bookingmanagement.exception;

public class PassengerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PassengerNotFoundException(String message) {
        super(message);
    }

    public PassengerNotFoundException(Long passengerId) {
        super("Passenger not found with ID: " + passengerId);
    }
}