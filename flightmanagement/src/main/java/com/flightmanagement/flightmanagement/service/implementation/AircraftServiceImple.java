package com.flightmanagement.flightmanagement.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flightmanagement.flightmanagement.service.interFace.AircraftService;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.AircraftReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.AircraftResDTO;
import com.flightmanagement.flightmanagement.entity.Aircraft;
import com.flightmanagement.flightmanagement.entity.Airline;
import com.flightmanagement.flightmanagement.mapper.AircraftMapper;
import com.flightmanagement.flightmanagement.repository.AircraftRepository;
import com.flightmanagement.flightmanagement.repository.AirlineRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AircraftServiceImple implements AircraftService {
    private final AircraftRepository aircraftRepository;
    private final AircraftMapper aircraftMapper;
    private final AirlineRepository airlineRepository;

    @Override
    @Transactional
    public AircraftResDTO createAircraft(AircraftReqDTO request) {

        if (aircraftRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new IllegalArgumentException(
                    "Aircraft with registration number " + request.getRegistrationNumber() + " already exists.");
        }

        Airline airline = airlineRepository.findById(request.getAirlineId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Airline with ID " + request.getAirlineId() + " not found."));

        Aircraft aircraft = Aircraft.builder()
                .registrationNumber(request.getRegistrationNumber().trim().toUpperCase())
                .model(request.getModel().trim())
                .manufacturer(request.getManufacturer().trim())
                .economySeats(request.getEconomySeats())
                .premiumEconomySeats(request.getPremiumEconomySeats())
                .businessSeats(request.getBusinessSeats())
                .firstClassSeats(request.getFirstClassSeats())
                .active(request.getActive() == null ? true : request.getActive())
                .airline(airline)
                .build();

        Aircraft savedAircraft = aircraftRepository.save(aircraft);

        return aircraftMapper.toDto(savedAircraft);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AircraftResDTO> getAllAircraft() {

        return aircraftRepository.findAll()
                .stream()
                .map(aircraftMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AircraftResDTO updateAircraft(Long aircraftId, AircraftReqDTO request) {

        Aircraft aircraft = aircraftRepository.findById(aircraftId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aircraft with ID " + aircraftId + " not found."));

        Airline airline = airlineRepository.findById(request.getAirlineId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Airline with ID " + request.getAirlineId() + " not found."));

        if (!aircraft.getRegistrationNumber()
                .equalsIgnoreCase(request.getRegistrationNumber().trim().toUpperCase())
                && aircraftRepository.existsByRegistrationNumber(
                        request.getRegistrationNumber().trim().toUpperCase())) {

            throw new IllegalArgumentException(
                    "Aircraft with registration number "
                            + request.getRegistrationNumber()
                            + " already exists.");
        }

        aircraft.setRegistrationNumber(request.getRegistrationNumber().trim().toUpperCase());
        aircraft.setModel(request.getModel().trim());
        aircraft.setManufacturer(request.getManufacturer().trim());

        aircraft.setEconomySeats(request.getEconomySeats());
        aircraft.setPremiumEconomySeats(request.getPremiumEconomySeats());
        aircraft.setBusinessSeats(request.getBusinessSeats());
        aircraft.setFirstClassSeats(request.getFirstClassSeats());

        aircraft.setActive(
                request.getActive() == null ? aircraft.getActive() : request.getActive());

        aircraft.setAirline(airline);

        Aircraft updatedAircraft = aircraftRepository.save(aircraft);

        return aircraftMapper.toDto(updatedAircraft);
    }

    @Override
    @Transactional(readOnly = true)
    public AircraftResDTO getAircraftById(Long id) {

        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aircraft with ID " + id + " not found."));

        return aircraftMapper.toDto(aircraft);
    }

    @Override
    @Transactional
    public void deleteAircraft(Long id) {

        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aircraft with ID " + id + " not found."));

        aircraftRepository.delete(aircraft);
    }

    @Override
    @Transactional
    public AircraftResDTO deactivateAircraft(Long aircraftId) {

        Aircraft aircraft = aircraftRepository.findById(aircraftId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aircraft with ID " + aircraftId + " not found."));

        aircraft.setActive(false);

        Aircraft updatedAircraft = aircraftRepository.save(aircraft);

        return aircraftMapper.toDto(updatedAircraft);
    }
}
