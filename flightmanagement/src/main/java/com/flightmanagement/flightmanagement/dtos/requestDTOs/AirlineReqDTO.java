package com.flightmanagement.flightmanagement.dtos.requestDTOs;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AirlineReqDTO {

    @NotBlank(message = "IATA code is required")
    @Pattern(regexp = "^[A-Z0-9]{2}$", message = "IATA code must be exactly 2 uppercase letters or digits")
    private String iataCode;

    @NotBlank(message = "Airline name is required")
    @Size(max = 100)
    private String name;

    @URL(message = "Invalid logo URL")
    @Size(max = 500)
    private String logoUrl;
}
