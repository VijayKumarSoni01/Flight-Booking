package com.flightmanagement.flightmanagement.service.implementation.publicImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightmanagement.flightmanagement.dtos.responseDTOs.BaggagePolicyResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightAmenityResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.PublicFlightResDTO;
import com.flightmanagement.flightmanagement.entity.BaggagePolicy;
import com.flightmanagement.flightmanagement.entity.Flight;
import com.flightmanagement.flightmanagement.entity.FlightAmenity;
import com.flightmanagement.flightmanagement.entity.FlightFare;
import com.flightmanagement.flightmanagement.entity.FlightInstance;
import com.flightmanagement.flightmanagement.mapper.FlightAmenityMapper;
import com.flightmanagement.flightmanagement.repository.FlightAmenityRepository;
import com.flightmanagement.flightmanagement.repository.FlightInstanceRepository;
import com.flightmanagement.flightmanagement.service.interFace.publicService.PublicFlightService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublicFlightServiceImpl
        implements PublicFlightService {

    private final FlightInstanceRepository flightInstanceRepository;

    private final FlightAmenityRepository flightAmenityRepository;

    private final FlightAmenityMapper flightAmenityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PublicFlightResDTO> searchFlights(
            String source,
            String destination,
            LocalDate date) {

        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException(
                    "Source is required.");
        }

        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException(
                    "Destination is required.");
        }

        if (date == null) {
            throw new IllegalArgumentException(
                    "Departure date is required.");
        }

        return flightInstanceRepository
                .searchPublicFlights(
                        source.trim(),
                        destination.trim(),
                        date)
                .stream()
                .map(this::mapFlightInstance)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PublicFlightResDTO getFlightDetails(
            Long flightInstanceId) {

        FlightInstance instance =
                flightInstanceRepository
                        .findById(flightInstanceId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Flight instance not found"));

        return mapFlightInstance(instance);
    }

    private PublicFlightResDTO mapFlightInstance(
            FlightInstance instance) {

        Flight flight =
                instance
                        .getSchedule()
                        .getFlight();

        PublicFlightResDTO dto =
                PublicFlightResDTO.builder()

                        .id(instance.getId())

                        .flightId(flight.getId())

                        .flightNumber(
                                flight.getFlightNumber())

                        .airlineName(
                                flight.getAirline().getName())

                        .airlineCode(
                                flight.getAirline().getIataCode())

                        .originAirportName(
                                flight
                                        .getOriginAirport()
                                        .getName())

                        .originAirportCode(
                                flight
                                        .getOriginAirport()
                                        .getIataCode())

                        .destinationAirportName(
                                flight
                                        .getDestinationAirport()
                                        .getName())

                        .destinationAirportCode(
                                flight
                                        .getDestinationAirport()
                                        .getIataCode())

                        .currency(
                                flight.getCurrency())

                        .departureTime(
                                instance.getDepartureTime())

                        .arrivalTime(
                                instance.getArrivalTime())

                        .departureTerminal(
                                flight.getDepartureTerminal())

                        .arrivalTerminal(
                                flight.getArrivalTerminal())

                        .durationMinutes(
                                flight.getDurationMinutes())

                        .build();

        mapFares(
                flight,
                dto);

        mapBaggage(
                flight,
                dto);

        mapAmenities(
                flight,
                dto);

        return dto;
    }

    private void mapFares(
            Flight flight,
            PublicFlightResDTO dto) {

        if (flight.getFlightFares() == null) {
            return;
        }

        for (FlightFare fare :
                flight.getFlightFares()) {

            switch (fare.getCabinClass()) {

                case ECONOMY:

                    dto.setEconomyPrice(
                            fare.getAdultFare());

                    break;

                case PREMIUM_ECONOMY:

                    dto.setPremiumEconomyPrice(
                            fare.getAdultFare());

                    break;

                case BUSINESS:

                    dto.setBusinessPrice(
                            fare.getAdultFare());

                    break;

                case FIRST:

                    dto.setFirstPrice(
                            fare.getAdultFare());

                    break;

                default:
                    break;
            }
        }
    }

    private void mapBaggage(
            Flight flight,
            PublicFlightResDTO dto) {

        if (flight.getBaggagePolicies() == null) {

            dto.setBaggagePolicies(
                    List.of());

            return;
        }

        dto.setBaggagePolicies(
                flight.getBaggagePolicies()
                        .stream()
                        .map(this::mapBaggage)
                        .toList());
    }

    private BaggagePolicyResDTO mapBaggage(
            BaggagePolicy baggage) {

        return BaggagePolicyResDTO.builder()

                .id(
                        baggage.getId())

                .flightId(
                        baggage
                                .getFlight()
                                .getId())

                .flightNumber(
                        baggage
                                .getFlight()
                                .getFlightNumber())

                .cabinClass(
                        baggage.getCabinClass())

                .cabinBaggageKg(
                        baggage.getCabinBaggageKg())

                .checkinBaggageKg(
                        baggage.getCheckinBaggageKg())

                .extraBaggagePricePerKg(
                        baggage
                                .getExtraBaggagePricePerKg())

                .build();
    }

    private void mapAmenities(
            Flight flight,
            PublicFlightResDTO dto) {

        FlightAmenity amenity =
                flightAmenityRepository
                        .findByFlightId(flight.getId())
                        .orElse(null);

        if (amenity == null) {
            dto.setAmenities(null);
            return;
        }

        FlightAmenityResDTO amenityDto =
                flightAmenityMapper.toDto(amenity);

        dto.setAmenities(amenityDto);
    }
}