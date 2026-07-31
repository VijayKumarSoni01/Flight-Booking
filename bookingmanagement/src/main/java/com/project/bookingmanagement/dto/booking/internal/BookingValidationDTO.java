package com.project.bookingmanagement.dto.booking.internal;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BookingValidationDTO {

    private boolean valid;

    private List<String> validationErrors = new ArrayList<>();
}
