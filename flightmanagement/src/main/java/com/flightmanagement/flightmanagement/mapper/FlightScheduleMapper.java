package com.flightmanagement.flightmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightScheduleResponseDTO;
import com.flightmanagement.flightmanagement.entity.FlightSchedule;

@Mapper(componentModel = "spring")
public interface FlightScheduleMapper {

    @Mapping(source = "flight.id", target = "flightId")
    @Mapping(source = "flightNumber", target = "flightNumber")
    @Mapping(source = "airline.name", target = "airlineName")
    @Mapping(source = "originAirport.iataCode", target = "originAirport")
    @Mapping(source = "destinationAirport.iataCode", target = "destinationAirport")
    @Mapping(source = "aircraft.model", target = "aircraft")
    FlightScheduleResponseDTO toDTO(FlightSchedule schedule);
}