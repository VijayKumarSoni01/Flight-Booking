package com.project.bookingmanagement.dto.passenger.internal;

import java.util.ArrayList;
import java.util.List;

import com.project.bookingmanagement.enums.validation.PassengerValidationError;

import lombok.Data;

@Data
public class PassengerValidationDTO {

    private boolean valid = true;

    private List<PassengerValidationError> validationErrors = new ArrayList<>();

    public void addValidationError(PassengerValidationError error) {
        if (error != null) {
            validationErrors.add(error);
            valid = false;
        }
    }

    public boolean isSuccessful() {
        return validationErrors.isEmpty();
    }
}
