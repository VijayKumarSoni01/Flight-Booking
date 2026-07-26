package com.flightmanagement.flightmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.AirportReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.AirportResDTO;
import com.flightmanagement.flightmanagement.entity.Airport;

@Mapper(componentModel = "spring")
public interface AirportMapper {

    AirportResDTO toDto(Airport airport);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Airport toEntity(AirportReqDTO request);
}