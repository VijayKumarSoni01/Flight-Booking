package com.flightmanagement.flightmanagement.service.interFace;

import java.time.LocalDate;
import java.util.List;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightResDTO;
import com.flightmanagement.flightmanagement.enums.FlightStatus;

public interface FlightService {

    FlightResDTO createFlight(FlightReqDTO request);

    FlightResDTO getFlightById(Long id);

    List<FlightResDTO> getAllFlights();

    List<FlightResDTO> searchFlights(
            Long originAirportId,
            Long destinationAirportId,
            LocalDate departureDate);

    FlightResDTO updateFlight(Long id, FlightReqDTO request);

    FlightResDTO updateFlightStatus(
            Long id,
            FlightStatus status);

    void deleteFlight(Long id);
}