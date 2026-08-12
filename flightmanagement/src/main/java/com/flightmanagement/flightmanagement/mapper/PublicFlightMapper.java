package com.flightmanagement.flightmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.flightmanagement.flightmanagement.dtos.responseDTOs.PublicFlightResDTO;
import com.flightmanagement.flightmanagement.entity.Flight;

@Mapper(componentModel = "spring")
public interface PublicFlightMapper {

        @Mapping(target = "airlineName", source = "airline.name")

        @Mapping(target = "airlineCode", source = "airline.iataCode")

        @Mapping(target = "originAirportName", source = "originAirport.name")

        @Mapping(target = "originAirportCode", source = "originAirport.iataCode")

        @Mapping(target = "destinationAirportName", source = "destinationAirport.name")

        @Mapping(target = "destinationAirportCode", source = "destinationAirport.iataCode")

        @Mapping(target = "economyPrice", ignore = true)

        @Mapping(target = "premiumEconomyPrice", ignore = true)

        @Mapping(target = "businessPrice", ignore = true)

        @Mapping(target = "firstPrice", ignore = true)

        @Mapping(target = "currency", source = "currency")

        @Mapping(target = "baggagePolicies", ignore = true)

        PublicFlightResDTO toDto(
                        Flight flight);

}