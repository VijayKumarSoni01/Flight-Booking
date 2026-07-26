package com.flightmanagement.flightmanagement.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightAmenityReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightAmenityResDTO;
import com.flightmanagement.flightmanagement.entity.Flight;
import com.flightmanagement.flightmanagement.entity.FlightAmenity;
import com.flightmanagement.flightmanagement.exception.ResourceAlreadyExistsException;
import com.flightmanagement.flightmanagement.exception.ResourceNotFoundException;
import com.flightmanagement.flightmanagement.mapper.FlightAmenityMapper;
import com.flightmanagement.flightmanagement.repository.FlightAmenityRepository;
import com.flightmanagement.flightmanagement.repository.FlightRepository;
import com.flightmanagement.flightmanagement.service.interFace.FlightAmenityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightAmenityServiceImple implements FlightAmenityService {

    private final FlightAmenityRepository flightAmenityRepository;
    private final FlightRepository flightRepository;
    private final FlightAmenityMapper flightAmenityMapper;

    @Override
    public FlightAmenityResDTO createAmenity(FlightAmenityReqDTO request) {

        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Flight with ID " + request.getFlightId() + " not found."));

        if (flightAmenityRepository.existsByFlightId(request.getFlightId())) {
            throw new ResourceAlreadyExistsException(
                    "Amenities already exist for Flight ID " + request.getFlightId() + ".");
        }

        FlightAmenity amenity = flightAmenityMapper.toEntity(request);
        amenity.setFlight(flight);

        FlightAmenity savedAmenity = flightAmenityRepository.save(amenity);

        return flightAmenityMapper.toDto(savedAmenity);
    }

    @Override
    @Transactional(readOnly = true)
    public FlightAmenityResDTO getAmenityById(Long id) {

        FlightAmenity amenity = flightAmenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Amenity with ID " + id + " not found."));

        return flightAmenityMapper.toDto(amenity);
    }

    @Override
    @Transactional(readOnly = true)
    public FlightAmenityResDTO getAmenityByFlightId(Long flightId) {

        FlightAmenity amenity = flightAmenityRepository.findByFlightId(flightId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Amenities not found for Flight ID " + flightId + "."));

        return flightAmenityMapper.toDto(amenity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightAmenityResDTO> getAllAmenities() {

        return flightAmenityRepository.findAll()
                .stream()
                .map(flightAmenityMapper::toDto)
                .toList();
    }

    @Override
    public FlightAmenityResDTO updateAmenity(Long id, FlightAmenityReqDTO request) {

        FlightAmenity amenity = flightAmenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Amenity with ID " + id + " not found."));

        flightAmenityMapper.updateEntityFromDto(request, amenity);

        FlightAmenity updatedAmenity = flightAmenityRepository.save(amenity);

        return flightAmenityMapper.toDto(updatedAmenity);
    }

    @Override
    public void deleteAmenity(Long id) {

        FlightAmenity amenity = flightAmenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Amenity with ID " + id + " not found."));

        flightAmenityRepository.delete(amenity);
    }
}