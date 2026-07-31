package com.project.bookingmanagement.dto.booking.request;

import java.util.List;

import com.project.bookingmanagement.dto.passenger.request.AddPassengerRequest;
import com.project.bookingmanagement.enums.bookingPassangerEnum.CabinClass;
import com.project.bookingmanagement.enums.paymentInfoEnum.PaymentMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBookingRequest {

    @NotNull(message = "Flight id is required")
    private Long flightId;

    @Email(message = "Invalid contact email")
    @NotBlank(message = "Contact email is required")
    private String contactEmail;

    @NotBlank(message = "Contact phone is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Invalid mobile number")
    private String contactPhone;

    @NotNull(message = "Cabin class is required")
    private CabinClass cabinClass;

    @Size(max = 500,
          message = "Special request cannot exceed 500 characters")
    private String specialRequest;

    @Size(max = 30,
          message = "Coupon code cannot exceed 30 characters")
    private String couponCode;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Valid
    @NotEmpty(message = "At least one passenger is required")
    @Size(min = 1, max = 9,
          message = "Maximum 9 passengers are allowed")
    private List<AddPassengerRequest> passengers;
}