package com.flightmanagement.flightmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.AirlineReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.AirlineResDTO;
import com.flightmanagement.flightmanagement.entity.Airline;

@Mapper(componentModel = "spring")
public interface AirlineMapper {

    AirlineResDTO toDto(Airline airline);

    @Mapping(target = "id", ignore = true)
    Airline toEntity(AirlineReqDTO request);
}