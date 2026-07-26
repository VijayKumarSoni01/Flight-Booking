package com.flightmanagement.flightmanagement.service.interFace;

import java.util.List;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightStatusInfoReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightStatusInfoResDTO;
import com.flightmanagement.flightmanagement.enums.FlightStatus;

public interface FlightStatusInfoService {

    FlightStatusInfoResDTO createFlightStatus(
            FlightStatusInfoReqDTO request);

    FlightStatusInfoResDTO getFlightStatusByFlightId(
            Long flightId);

    List<FlightStatusInfoResDTO> getAllFlightStatuses();

    List<FlightStatusInfoResDTO> getFlightStatusesByStatus(
            FlightStatus status);

    FlightStatusInfoResDTO updateFlightStatus(
            Long flightId,
            FlightStatusInfoReqDTO request);

    void deleteFlightStatus(
            Long flightId);
}