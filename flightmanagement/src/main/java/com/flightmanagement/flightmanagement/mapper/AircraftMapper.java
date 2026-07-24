package com.flightmanagement.flightmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.AircraftReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.AircraftResDTO;
import com.flightmanagement.flightmanagement.entity.Aircraft;

@Mapper(componentModel = "spring")
public interface AircraftMapper {

    @Mapping(source = "airline.id", target = "airlineId")
    @Mapping(source = "airline.name", target = "airlineName")
    AircraftResDTO toDto(Aircraft aircraft);

    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "id", ignore = true)
    Aircraft toEntity(AircraftReqDTO request);
}