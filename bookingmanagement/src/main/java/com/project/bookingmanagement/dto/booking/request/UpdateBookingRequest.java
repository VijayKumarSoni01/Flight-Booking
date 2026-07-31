package com.project.bookingmanagement.dto.booking.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateBookingRequest {

    @Email(message = "Invalid contact email")
    @NotBlank(message = "Contact email is required")
    private String contactEmail;

    @NotBlank(message = "Contact phone is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid mobile number")
    private String contactPhone;

    @Size(
            max = 500,
            message = "Special request cannot exceed 500 characters")
    private String specialRequest;
}