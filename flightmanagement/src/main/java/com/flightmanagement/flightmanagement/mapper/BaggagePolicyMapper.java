package com.flightmanagement.flightmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.BaggagePolicyReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.BaggagePolicyResDTO;
import com.flightmanagement.flightmanagement.entity.BaggagePolicy;

@Mapper(componentModel = "spring")
public interface BaggagePolicyMapper {

    @Mapping(source = "flight.id", target = "flightId")
    @Mapping(source = "flight.flightNumber", target = "flightNumber")
    @Mapping(source = "cabinClass", target = "cabinClass")
    BaggagePolicyResDTO toDto(BaggagePolicy baggagePolicy);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", ignore = true)
    BaggagePolicy toEntity(BaggagePolicyReqDTO request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", ignore = true)
    void updateEntityFromDto(
            BaggagePolicyReqDTO request,
            @MappingTarget BaggagePolicy baggagePolicy);
}