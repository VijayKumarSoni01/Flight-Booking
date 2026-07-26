package com.flightmanagement.flightmanagement.service.interFace;

import java.util.List;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightFareReqDTO;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightFareUpdateReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightFareResDTO;
import com.flightmanagement.flightmanagement.enums.CabinClass;

public interface FlightFareService {

    FlightFareResDTO createFare(FlightFareReqDTO request);

    List<FlightFareResDTO> getFaresByFlightId(Long flightId);

    FlightFareResDTO getFareByFlightIdAndCabinClass(Long flightId, CabinClass cabinClass);

    List<FlightFareResDTO> getAllFares();

    FlightFareResDTO updateFare(
            Long flightId,
            CabinClass cabinClass,
            FlightFareUpdateReqDTO request);

    void deleteFare(Long id);
}