package com.flightmanagement.flightmanagement.dtos.requestDTOs.publicFlight;


import java.time.LocalDate;

import com.flightmanagement.flightmanagement.enums.CabinClass;

import lombok.Data;


@Data
public class FlightSearchRequestDTO {


    private String source;


    private String destination;


    private LocalDate date;


    private CabinClass cabinClass;

}