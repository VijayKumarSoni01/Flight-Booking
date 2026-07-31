package com.project.bookingmanagement.exception;

import java.util.List;

import lombok.Getter;

@Getter
public class BookingValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final List<String> validationErrors;

    public BookingValidationException(List<String> validationErrors) {
        super("Booking validation failed.");
        this.validationErrors = validationErrors;
    }
}
