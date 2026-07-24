package com.flightmanagement.flightmanagement.service.interFace;

import java.util.List;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.AirlineReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.AirlineResDTO;

public interface AirlineService{
    AirlineResDTO createAirline(AirlineReqDTO request);

    AirlineResDTO getAirlineById(Long airlineId);

    List<AirlineResDTO> getAllAirlines();

    AirlineResDTO updateAirline(Long airlineId, AirlineReqDTO request);

    void deactivateAirline(Long airlineId);

    void deleteAirline(Long airlineId);
}
