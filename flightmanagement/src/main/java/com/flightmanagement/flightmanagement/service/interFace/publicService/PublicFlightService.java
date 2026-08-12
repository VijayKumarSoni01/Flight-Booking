package com.flightmanagement.flightmanagement.service.interFace.publicService;


import java.time.LocalDate;
import java.util.List;

import com.flightmanagement.flightmanagement.dtos.responseDTOs.PublicFlightResDTO;


public interface PublicFlightService {


    List<PublicFlightResDTO> searchFlights(
            String source,
            String destination,
            LocalDate date
    );


    PublicFlightResDTO getFlightDetails(
            Long flightId
    );

}