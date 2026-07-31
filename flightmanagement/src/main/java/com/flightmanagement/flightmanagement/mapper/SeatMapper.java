package com.flightmanagement.flightmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.SeatReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatResDTO;
import com.flightmanagement.flightmanagement.entity.Seat;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(target = "flightId", source = "flight.id")
    @Mapping(target = "flightNumber", source = "flight.flightNumber")
    SeatResDTO toDto(Seat seat);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", ignore = true)
    @Mapping(target = "reservedAt", ignore = true)
    @Mapping(target = "seatIndex", ignore = true)   // <-- Add this
    Seat toEntity(SeatReqDTO request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", ignore = true)
    @Mapping(target = "reservedAt", ignore = true)
    @Mapping(target = "seatIndex", ignore = true)   // <-- Add this
    void updateEntityFromDto(
            SeatReqDTO request,
            @MappingTarget Seat seat);
}