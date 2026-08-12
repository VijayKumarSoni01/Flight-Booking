package com.project.bookingmanagement.dto.booking.request;

import java.math.BigDecimal;
import java.util.List;

import com.project.bookingmanagement.dto.passenger.request.AddPassengerRequest;
import com.project.bookingmanagement.enums.bookingPassangerEnum.CabinClass;
import com.project.bookingmanagement.enums.paymentInfoEnum.PaymentMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateBookingRequest {

      @NotNull(message = "Flight id is required")
      private Long flightId;

      @NotNull(message = "Fare id is required")
      private Long fareId;

      @NotNull(message = "Total amount is required")
      private BigDecimal totalAmount;

      @Email(message = "Invalid contact email")
      @NotBlank(message = "Contact email is required")
      private String contactEmail;

      @NotBlank(message = "Contact phone is required")
      @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
      private String contactPhone;

      @NotNull(message = "Cabin class is required")
      private CabinClass cabinClass;

      @Size(max = 500)
      private String specialRequest;

      @Size(max = 30)
      private String couponCode;

      @NotNull(message = "Payment method is required")
      private PaymentMethod paymentMethod;

      @Valid
      @NotEmpty(message = "At least one passenger is required")
      @Size(min = 1, max = 9)
      private List<AddPassengerRequest> passengers;
}