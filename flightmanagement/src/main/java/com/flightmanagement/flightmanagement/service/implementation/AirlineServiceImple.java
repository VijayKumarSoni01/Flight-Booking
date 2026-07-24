package com.flightmanagement.flightmanagement.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flightmanagement.flightmanagement.service.interFace.AirlineService;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.AirlineReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.AirlineResDTO;
import com.flightmanagement.flightmanagement.entity.Airline;
import com.flightmanagement.flightmanagement.mapper.AirlineMapper;
import com.flightmanagement.flightmanagement.repository.AirlineRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AirlineServiceImple implements AirlineService {

    private final AirlineRepository airlineRepository;
    private final AirlineMapper airlineMapper;

    @Override
    @Transactional
    public AirlineResDTO createAirline(AirlineReqDTO request) {

        String iataCode = request.getIataCode().trim().toUpperCase();
        String name = request.getName().trim();

        if (airlineRepository.existsByIataCode(iataCode)) {
            throw new IllegalArgumentException(
                    "Airline with IATA code '" + iataCode + "' already exists.");
        }

        if (airlineRepository.existsByName(name)) {
            throw new IllegalArgumentException(
                    "Airline with name '" + name + "' already exists.");
        }

        Airline airline = Airline.builder()
                .iataCode(iataCode)
                .name(name)
                .logoUrl(request.getLogoUrl())
                .build();

        Airline savedAirline = airlineRepository.save(airline);

        return airlineMapper.toDto(savedAirline);
    }

    @Override
    @Transactional(readOnly = true)
    public AirlineResDTO getAirlineById(Long airlineId) {

        Airline airline = getAirline(airlineId);

        return airlineMapper.toDto(airline);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AirlineResDTO> getAllAirlines() {

        return airlineRepository.findAll()
                .stream()
                .map(airlineMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AirlineResDTO updateAirline(Long airlineId, AirlineReqDTO request) {

        Airline airline = getAirline(airlineId);

        String iataCode = request.getIataCode().trim().toUpperCase();
        String name = request.getName().trim();

        if (airlineRepository.existsByIataCodeAndIdNot(iataCode, airlineId)) {
            throw new IllegalArgumentException(
                    "Airline with IATA code '" + iataCode + "' already exists.");
        }

        if (airlineRepository.existsByNameAndIdNot(name, airlineId)) {
            throw new IllegalArgumentException(
                    "Airline with name '" + name + "' already exists.");
        }

        airline.setIataCode(iataCode);
        airline.setName(name);
        airline.setLogoUrl(request.getLogoUrl());

        Airline updatedAirline = airlineRepository.save(airline);

        return airlineMapper.toDto(updatedAirline);
    }

    @Override
    @Transactional
    public void deactivateAirline(Long airlineId) {

        Airline airline = getAirline(airlineId);

        airline.setActive(false);

        airlineRepository.save(airline);
    }

    @Override
    @Transactional
    public void deleteAirline(Long airlineId) {

        Airline airline = getAirline(airlineId);
        airlineRepository.delete(airline);
    }

    private Airline getAirline(Long airlineId) {

        return airlineRepository.findById(airlineId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Airline with ID " + airlineId + " not found."));
    }
}