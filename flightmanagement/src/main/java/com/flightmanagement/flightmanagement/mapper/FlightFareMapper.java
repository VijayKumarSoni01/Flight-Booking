package com.flightmanagement.flightmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightFareReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightFareResDTO;
import com.flightmanagement.flightmanagement.entity.FlightFare;

@Mapper(componentModel = "spring")
public interface FlightFareMapper {

    @Mapping(target = "flightId", source = "flight.id")
    @Mapping(target = "flightNumber", source = "flight.flightNumber")
    FlightFareResDTO toDto(FlightFare fare);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", ignore = true)
    FlightFare toEntity(FlightFareReqDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", ignore = true)
    void updateEntityFromDto(
            FlightFareReqDTO dto,
            @MappingTarget FlightFare fare);
}