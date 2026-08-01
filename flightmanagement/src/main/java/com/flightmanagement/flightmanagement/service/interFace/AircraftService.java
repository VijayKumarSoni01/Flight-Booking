package com.flightmanagement.flightmanagement.service.interFace;

import java.util.List;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.AircraftReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.AircraftResDTO;
import com.flightmanagement.flightmanagement.entity.Aircraft;
import com.flightmanagement.flightmanagement.enums.CabinClass;

public interface AircraftService {
    
    AircraftResDTO createAircraft(AircraftReqDTO request);

    AircraftResDTO getAircraftById(Long aircraftId);

    List<AircraftResDTO> getAllAircraft();

    AircraftResDTO updateAircraft(Long aircraftId, AircraftReqDTO request);

    void deleteAircraft(Long aircraftId);

    AircraftResDTO deactivateAircraft(Long aircraftId);

    boolean isCabinAvailable(Long aircraftId, CabinClass cabinClass);
    
    void validateCabinAvailability(Aircraft aircraft, CabinClass cabinClass);
}
