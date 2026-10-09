package com.flightmanagement.flightmanagement.service.implementation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightAmenityResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightResDTO;
import com.flightmanagement.flightmanagement.entity.Aircraft;
import com.flightmanagement.flightmanagement.entity.Airline;
import com.flightmanagement.flightmanagement.entity.Airport;
import com.flightmanagement.flightmanagement.entity.Flight;
import com.flightmanagement.flightmanagement.entity.FlightInstance;
import com.flightmanagement.flightmanagement.enums.FlightStatus;
import com.flightmanagement.flightmanagement.mapper.FlightAmenityMapper;
import com.flightmanagement.flightmanagement.mapper.FlightMapper;
import com.flightmanagement.flightmanagement.repository.AircraftRepository;
import com.flightmanagement.flightmanagement.repository.AirportRepository;
import com.flightmanagement.flightmanagement.repository.FlightAmenityRepository;
import com.flightmanagement.flightmanagement.repository.FlightInstanceRepository;
import com.flightmanagement.flightmanagement.repository.FlightRepository;
import com.flightmanagement.flightmanagement.service.interFace.FlightService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightServiceImple implements FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;

    private final AircraftRepository aircraftRepository;
    private final AirportRepository airportRepository;
    private final FlightInstanceRepository flightInstanceRepository;

    private final FlightAmenityRepository flightAmenityRepository;
    private final FlightAmenityMapper flightAmenityMapper;

    @Override
    @Transactional
    public FlightResDTO createFlight(FlightReqDTO request) {

        if (request.getOriginAirportId()
                .equals(request.getDestinationAirportId())) {

            throw new IllegalArgumentException(
                    "Origin and destination airports cannot be the same.");
        }

        if (!request.getArrivalTime()
                .isAfter(request.getDepartureTime())) {

            throw new IllegalArgumentException(
                    "Arrival time must be after departure time.");
        }

        String flightNumber =
                normalizeFlightNumber(request.getFlightNumber());

        Aircraft aircraft =
                aircraftRepository.findById(request.getAircraftId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Aircraft with ID "
                                                + request.getAircraftId()
                                                + " not found."));

        if (!aircraft.getActive()) {
            throw new IllegalArgumentException(
                    "Aircraft with ID "
                            + request.getAircraftId()
                            + " is inactive.");
        }

        Airline airline = aircraft.getAirline();

        if (airline == null) {
            throw new IllegalStateException(
                    "Aircraft with id "
                            + aircraft.getId()
                            + " has no associated airline.");
        }

        Airport originAirport =
                airportRepository.findById(
                        request.getOriginAirportId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Origin airport with ID "
                                                + request.getOriginAirportId()
                                                + " not found."));

        Airport destinationAirport =
                airportRepository.findById(
                        request.getDestinationAirportId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Destination airport with ID "
                                                + request.getDestinationAirportId()
                                                + " not found."));

        if (flightRepository.existsByFlightNumberAndDepartureTime(
                flightNumber,
                request.getDepartureTime())) {

            throw new IllegalArgumentException(
                    "A flight with this flight number and departure time already exists.");
        }

        Flight flight = flightMapper.toEntity(request);

        flight.setFlightNumber(flightNumber);
        flight.setAircraft(aircraft);
        flight.setAirline(airline);
        flight.setOriginAirport(originAirport);
        flight.setDestinationAirport(destinationAirport);

        Flight saved = flightRepository.save(flight);

        FlightResDTO dto = flightMapper.toDto(saved);

        populateAmenities(dto, saved.getId());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public FlightResDTO getFlightById(Long id) {

        Flight flight =
                flightRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Flight not found"));

        FlightResDTO dto = flightMapper.toDto(flight);

        populateAmenities(dto, flight.getId());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightResDTO> getAllFlights() {

        return flightRepository.findAll()
                .stream()
                .map(flight -> {

                    FlightResDTO dto =
                            flightMapper.toDto(flight);

                    populateAmenities(
                            dto,
                            flight.getId());

                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightResDTO> searchFlights(
            Long originAirportId,
            Long destinationAirportId,
            LocalDate departureDate) {

        if (originAirportId.equals(destinationAirportId)) {
            throw new IllegalArgumentException(
                    "Origin and destination airports cannot be the same.");
        }

        List<FlightInstance> instances =
                flightInstanceRepository.searchFlights(
                        originAirportId,
                        destinationAirportId,
                        departureDate);

        return instances.stream()
                .map(instance -> {

                    Flight flight =
                            instance.getSchedule().getFlight();

                    FlightResDTO dto =
                            flightMapper.toDto(flight);

                    dto.setId(instance.getId());

                    dto.setDepartureTime(
                            instance.getDepartureTime());

                    dto.setArrivalTime(
                            instance.getArrivalTime());

                    populateAmenities(
                            dto,
                            flight.getId());

                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional
    public FlightResDTO updateFlight(
            Long id,
            FlightReqDTO request) {

        Flight flight =
                flightRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Flight with ID "
                                                + id
                                                + " not found."));

        if (request.getOriginAirportId()
                .equals(request.getDestinationAirportId())) {

            throw new IllegalArgumentException(
                    "Origin and destination airports cannot be the same.");
        }

        if (!request.getArrivalTime()
                .isAfter(request.getDepartureTime())) {

            throw new IllegalArgumentException(
                    "Arrival time must be after departure time.");
        }

        String flightNumber =
                normalizeFlightNumber(
                        request.getFlightNumber());

        if (flightRepository
                .existsByFlightNumberAndDepartureTimeAndIdNot(
                        flightNumber,
                        request.getDepartureTime(),
                        id)) {

            throw new IllegalArgumentException(
                    "A flight with this flight number and departure time already exists.");
        }

        Aircraft aircraft =
                aircraftRepository.findById(
                        request.getAircraftId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Aircraft with ID "
                                                + request.getAircraftId()
                                                + " not found."));

        if (!aircraft.getActive()) {
            throw new IllegalArgumentException(
                    "Aircraft with ID "
                            + request.getAircraftId()
                            + " is inactive.");
        }

        Airport originAirport =
                airportRepository.findById(
                        request.getOriginAirportId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Origin airport with ID "
                                                + request.getOriginAirportId()
                                                + " not found."));

        Airport destinationAirport =
                airportRepository.findById(
                        request.getDestinationAirportId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Destination airport with ID "
                                                + request.getDestinationAirportId()
                                                + " not found."));

        flightMapper.updateEntityFromDto(
                request,
                flight);

        flight.setFlightNumber(flightNumber);
        flight.setAircraft(aircraft);
        flight.setAirline(aircraft.getAirline());
        flight.setOriginAirport(originAirport);
        flight.setDestinationAirport(destinationAirport);

        Flight updatedFlight =
                flightRepository.save(flight);

        FlightResDTO dto =
                flightMapper.toDto(updatedFlight);

        populateAmenities(
                dto,
                updatedFlight.getId());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean validateFlight(Long flightId) {

        flightRepository.findById(flightId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Flight with ID "
                                        + flightId
                                        + " not found."));

        return true;
    }

    @Override
    @Transactional
    public FlightResDTO updateFlightStatus(
            Long id,
            FlightStatus status) {

        Flight flight =
                flightRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Flight with ID "
                                                + id
                                                + " not found."));

        validateStatusTransition(
                flight.getStatus(),
                status);

        if (status == FlightStatus.LANDED
                && flight.getStatus() != FlightStatus.DEPARTED) {

            throw new IllegalArgumentException(
                    "Only a departed flight can be marked as LANDED.");
        }

        flight.setStatus(status);

        Flight updatedFlight =
                flightRepository.save(flight);

        FlightResDTO dto =
                flightMapper.toDto(updatedFlight);

        populateAmenities(
                dto,
                updatedFlight.getId());

        return dto;
    }

    @Override
    @Transactional
    public void deleteFlight(Long id) {

        Flight flight =
                flightRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Flight with ID "
                                                + id
                                                + " not found."));

        flightRepository.delete(flight);
    }

    private String normalizeFlightNumber(
            String flightNumber) {

        return flightNumber
                .trim()
                .toUpperCase();
    }

    private void validateStatusTransition(
            FlightStatus current,
            FlightStatus next) {

        if (current == next) {
            throw new IllegalArgumentException(
                    "Flight is already in status "
                            + current
                            + ".");
        }

        if (current == FlightStatus.CANCELLED
                || current == FlightStatus.LANDED) {

            throw new IllegalStateException(
                    "Cannot change status of a flight that is already "
                            + current
                            + ".");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FlightResDTO getFlightInstanceById(
            Long flightInstanceId) {

        FlightInstance instance =
                flightInstanceRepository.findById(
                        flightInstanceId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Flight Instance with ID "
                                                + flightInstanceId
                                                + " not found."));

        Flight flight =
                instance
                        .getSchedule()
                        .getFlight();

        FlightResDTO dto =
                flightMapper.toDto(flight);

        dto.setId(instance.getId());

        dto.setDepartureTime(
                instance.getDepartureTime());

        dto.setArrivalTime(
                instance.getArrivalTime());

        populateAmenities(
                dto,
                flight.getId());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean validateFlightInstance(
            Long flightInstanceId) {

        FlightInstance instance =
                flightInstanceRepository.findById(
                        flightInstanceId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Flight Instance with ID "
                                                + flightInstanceId
                                                + " not found."));

        return instance.getStatus()
                != FlightStatus.CANCELLED;
    }

    private void populateAmenities(
            FlightResDTO dto,
            Long flightId) {

        FlightAmenityResDTO amenities =
                flightAmenityRepository
                        .findByFlightId(flightId)
                        .map(flightAmenityMapper::toDto)
                        .orElse(null);

        dto.setAmenities(amenities);
    }
}