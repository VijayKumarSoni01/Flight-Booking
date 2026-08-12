package com.flightmanagement.flightmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightResDTO;
import com.flightmanagement.flightmanagement.entity.Flight;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    @Mapping(source = "airline.id", target = "airlineId")
    @Mapping(source = "airline.name", target = "airlineName")
    @Mapping(source = "airline.iataCode", target = "airlineCode")

    @Mapping(source = "aircraft.id", target = "aircraftId")
    @Mapping(source = "aircraft.registrationNumber", target = "aircraftRegistration")
    @Mapping(source = "aircraft.model", target = "aircraftModel")

    @Mapping(source = "originAirport.id", target = "originAirportId")
    @Mapping(source = "originAirport.name", target = "originAirportName")
    @Mapping(source = "originAirport.iataCode", target = "originAirportCode")

    @Mapping(source = "destinationAirport.id", target = "destinationAirportId")
    @Mapping(source = "destinationAirport.name", target = "destinationAirportName")
    @Mapping(source = "destinationAirport.iataCode", target = "destinationAirportCode")

    // ADD THIS
    @Mapping(source = "currency", target = "currency")

    FlightResDTO toDto(Flight flight);

    @Mapping(target = "id", ignore = true)

    @Mapping(target = "airline", ignore = true)

    @Mapping(target = "aircraft", ignore = true)

    @Mapping(target = "originAirport", ignore = true)

    @Mapping(target = "destinationAirport", ignore = true)

    @Mapping(target = "status", ignore = true)

    @Mapping(target = "durationMinutes", ignore = true)

    @Mapping(target = "createdAt", ignore = true)

    @Mapping(target = "updatedAt", ignore = true)

    @Mapping(target = "version", ignore = true)

    Flight toEntity(FlightReqDTO request);

    @Mapping(target = "id", ignore = true)

    @Mapping(target = "airline", ignore = true)

    @Mapping(target = "aircraft", ignore = true)

    @Mapping(target = "originAirport", ignore = true)

    @Mapping(target = "destinationAirport", ignore = true)

    @Mapping(target = "status", ignore = true)

    @Mapping(target = "durationMinutes", ignore = true)

    @Mapping(target = "createdAt", ignore = true)

    @Mapping(target = "updatedAt", ignore = true)

    @Mapping(target = "version", ignore = true)

    void updateEntityFromDto(
            FlightReqDTO request,
            @MappingTarget Flight flight);

}