package com.flightmanagement.flightmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightAmenityReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightAmenityResDTO;
import com.flightmanagement.flightmanagement.entity.FlightAmenity;

@Mapper(componentModel = "spring")
public interface FlightAmenityMapper {

    @Mapping(target = "flightId", source = "flight.id")
    @Mapping(target = "flightNumber", source = "flight.flightNumber")
    FlightAmenityResDTO toDto(FlightAmenity amenity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", ignore = true)
    FlightAmenity toEntity(FlightAmenityReqDTO request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", ignore = true)
    void updateEntityFromDto(
            FlightAmenityReqDTO request,
            @MappingTarget FlightAmenity amenity);
}