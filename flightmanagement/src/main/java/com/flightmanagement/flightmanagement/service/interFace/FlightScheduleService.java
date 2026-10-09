package com.flightmanagement.flightmanagement.service.interFace;

import java.util.List;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightScheduleRequest;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightScheduleResponseDTO;
public interface FlightScheduleService {

    FlightScheduleResponseDTO  createSchedule(
            FlightScheduleRequest request
    );

    List<FlightScheduleResponseDTO> getAllSchedules();

    List<FlightScheduleResponseDTO> getSchedulesByFlight(
            Long flightId
    );

    FlightScheduleResponseDTO getScheduleById(
            Long scheduleId
    );

    void deleteSchedule(
            Long scheduleId
    );
}