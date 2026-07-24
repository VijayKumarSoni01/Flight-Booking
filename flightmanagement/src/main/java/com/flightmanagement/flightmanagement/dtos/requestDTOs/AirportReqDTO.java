package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AirportReqDTO {

    @NotBlank(message = "IATA code is required")
    @Size(min = 3, max = 3, message = "IATA code must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "IATA code must contain only uppercase letters")
    private String iataCode;

    @NotBlank(message = "ICAO code is required")
    @Size(min = 4, max = 4, message = "ICAO code must be exactly 4 characters")
    @Pattern(regexp = "^[A-Z]{4}$", message = "ICAO code must contain only uppercase letters")
    private String icaoCode;

    @NotBlank(message = "Airport name is required")
    @Size(max = 100, message = "Airport name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90")
    @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90")
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180")
    @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180")
    private Double longitude;

    @NotBlank(message = "Timezone is required")
    @Size(max = 50, message = "Timezone cannot exceed 50 characters")
    private String timezone;
}
