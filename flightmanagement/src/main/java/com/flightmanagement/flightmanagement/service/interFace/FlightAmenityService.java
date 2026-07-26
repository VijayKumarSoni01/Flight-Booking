package com.flightmanagement.flightmanagement.service.interFace;

import java.util.List;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightAmenityReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightAmenityResDTO;

public interface FlightAmenityService {

    FlightAmenityResDTO createAmenity(FlightAmenityReqDTO request);

    FlightAmenityResDTO updateAmenity(Long id, FlightAmenityReqDTO request);

    FlightAmenityResDTO getAmenityById(Long id);

    FlightAmenityResDTO getAmenityByFlightId(Long flightId);

    List<FlightAmenityResDTO> getAllAmenities();

    void deleteAmenity(Long id);
}
