package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AircraftReqDTO {

    @NotBlank(message = "Registration number is required")
    @Size(min = 3, max = 20, message = "Registration number must be between 3 and 20 characters")
    @Pattern(
            regexp = "^[A-Z0-9-]+$",
            message = "Registration number can contain only uppercase letters, numbers, and hyphens"
    )
    private String registrationNumber;

    @NotBlank(message = "Aircraft model is required")
    @Size(max = 50, message = "Model cannot exceed 50 characters")
    private String model;

    @NotBlank(message = "Manufacturer is required")
    @Size(max = 50, message = "Manufacturer cannot exceed 50 characters")
    private String manufacturer;

    @NotNull(message = "Economy seats are required")
    @Min(value = 0, message = "Economy seats cannot be negative")
    private Integer economySeats;

    @NotNull(message = "Premium economy seats are required")
    @Min(value = 0, message = "Premium economy seats cannot be negative")
    private Integer premiumEconomySeats;

    @NotNull(message = "Business seats are required")
    @Min(value = 0, message = "Business seats cannot be negative")
    private Integer businessSeats;

    @NotNull(message = "First class seats are required")
    @Min(value = 0, message = "First class seats cannot be negative")
    private Integer firstClassSeats;

    private Boolean active;

    @NotNull(message = "Airline ID is required")
    private Long airlineId;
}