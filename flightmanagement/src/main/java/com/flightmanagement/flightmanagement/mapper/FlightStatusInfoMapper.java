package com.flightmanagement.flightmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightStatusInfoReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightStatusInfoResDTO;
import com.flightmanagement.flightmanagement.entity.FlightStatusInfo;

@Mapper(componentModel = "spring")
public interface FlightStatusInfoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", ignore = true)
    @Mapping(target = "lastApiSync", ignore = true)
    FlightStatusInfo toEntity(FlightStatusInfoReqDTO request);

    @Mapping(target = "flightId", source = "flight.id")
    @Mapping(target = "flightNumber", source = "flight.flightNumber")
    FlightStatusInfoResDTO toDto(FlightStatusInfo entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", ignore = true)
    @Mapping(target = "lastApiSync", ignore = true)
    void updateEntityFromDto(
            FlightStatusInfoReqDTO request,
            @MappingTarget FlightStatusInfo entity);
}