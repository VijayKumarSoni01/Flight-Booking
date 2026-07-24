package com.flightmanagement.flightmanagement.service.interFace;

import java.util.List;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.AirportReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.AirportResDTO;

public interface AirportServices {

    AirportResDTO createAirport(AirportReqDTO request);
    
    AirportResDTO getAirportById(Long id);

    AirportResDTO updateAirport(Long id, AirportReqDTO request);

    void deactivateAirport(Long id);

    List<AirportResDTO> getAllAirports();

    void deleteAirport(Long id);
}
