package com.flightmanagement.flightmanagement.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightmanagement.flightmanagement.service.interFace.AirportServices;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.AirportReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.AirportResDTO;
import com.flightmanagement.flightmanagement.entity.Airport;
import com.flightmanagement.flightmanagement.mapper.AirportMapper;
import com.flightmanagement.flightmanagement.repository.AirportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AirportServiceImple implements AirportServices {

    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;

    @Override
    @Transactional
    public AirportResDTO createAirport(AirportReqDTO request) {

        String iataCode = request.getIataCode().trim().toUpperCase();
        String icaoCode = request.getIcaoCode().trim().toUpperCase();

        if (airportRepository.existsByIataCode(iataCode)) {
            throw new IllegalArgumentException(
                    "Airport with IATA code '" + iataCode + "' already exists.");
        }

        if (airportRepository.existsByIcaoCode(icaoCode)) {
            throw new IllegalArgumentException(
                    "Airport with ICAO code '" + icaoCode + "' already exists.");
        }

        Airport airport = Airport.builder()
                .iataCode(iataCode)
                .icaoCode(icaoCode)
                .name(request.getName().trim())
                .city(request.getCity().trim())
                .country(request.getCountry().trim())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .timezone(request.getTimezone().trim())
                .active(true)
                .build();

        return airportMapper.toDto(airportRepository.save(airport));
    }

    @Override
    @Transactional(readOnly = true)
    public AirportResDTO getAirportById(Long id) {

        Airport airport = getAirport(id);

        return airportMapper.toDto(airport);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AirportResDTO> getAllAirports() {

        return airportRepository.findByActiveTrue()
                .stream()
                .map(airportMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AirportResDTO updateAirport(Long id, AirportReqDTO request) {

        Airport airport = getAirport(id);

        String iataCode = request.getIataCode().trim().toUpperCase();
        String icaoCode = request.getIcaoCode().trim().toUpperCase();

        if (airportRepository.existsByIataCodeAndIdNot(iataCode, id)) {
            throw new IllegalArgumentException(
                    "Airport with IATA code '" + iataCode + "' already exists.");
        }

        if (airportRepository.existsByIcaoCodeAndIdNot(icaoCode, id)) {
            throw new IllegalArgumentException(
                    "Airport with ICAO code '" + icaoCode + "' already exists.");
        }

        airport.setIataCode(iataCode);
        airport.setIcaoCode(icaoCode);
        airport.setName(request.getName().trim());
        airport.setCity(request.getCity().trim());
        airport.setCountry(request.getCountry().trim());
        airport.setLatitude(request.getLatitude());
        airport.setLongitude(request.getLongitude());
        airport.setTimezone(request.getTimezone().trim());

        Airport updatedAirport = airportRepository.save(airport);

        return airportMapper.toDto(updatedAirport);
    }

    @Override
    @Transactional
    public void deactivateAirport(Long id) {

        Airport airport = getAirport(id);

        airport.setActive(false);

        airportRepository.save(airport);
    }

    @Override
    @Transactional
    public void deleteAirport(Long id) {
        Airport airport = getAirport(id);
        airportRepository.delete(airport);
    }

    private Airport getAirport(Long id) {

        return airportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Airport with ID " + id + " not found."));
    }
}