package com.flightmanagement.flightmanagement.dtos.responseDTOs;

import lombok.Data;

@Data
public class AircraftResDTO {
    
    private Long id;

    private String registrationNumber;

    private String model;

    private String manufacturer;

    private Integer economySeats;

    private Integer premiumEconomySeats;

    private Integer businessSeats;

    private Integer firstClassSeats;

    private Integer totalSeats;

    private Boolean active;

    private Long airlineId;

    private String airlineName;
}
