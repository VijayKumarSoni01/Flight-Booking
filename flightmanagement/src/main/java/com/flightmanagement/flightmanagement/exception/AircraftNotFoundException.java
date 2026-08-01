package com.flightmanagement.flightmanagement.exception;

public class AircraftNotFoundException extends RuntimeException {

    public AircraftNotFoundException(Long id) {
        super("Aircraft with ID " + id + " not found.");
    }
}