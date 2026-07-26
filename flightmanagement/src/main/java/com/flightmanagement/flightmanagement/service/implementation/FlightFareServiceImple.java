package com.flightmanagement.flightmanagement.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightFareReqDTO;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightFareUpdateReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightFareResDTO;
import com.flightmanagement.flightmanagement.entity.Flight;
import com.flightmanagement.flightmanagement.entity.FlightFare;
import com.flightmanagement.flightmanagement.enums.CabinClass;
import com.flightmanagement.flightmanagement.exception.ResourceAlreadyExistsException;
import com.flightmanagement.flightmanagement.exception.ResourceNotFoundException;
import com.flightmanagement.flightmanagement.mapper.FlightFareMapper;
import com.flightmanagement.flightmanagement.repository.FlightFareRepository;
import com.flightmanagement.flightmanagement.repository.FlightRepository;
import com.flightmanagement.flightmanagement.service.interFace.FlightFareService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightFareServiceImple implements FlightFareService {

    private final FlightFareRepository flightFareRepository;
    private final FlightRepository flightRepository;
    private final FlightFareMapper flightFareMapper;

    @Override
    public FlightFareResDTO createFare(FlightFareReqDTO request) {

        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Flight with ID " + request.getFlightId() + " not found."));

        if (flightFareRepository.existsByFlightIdAndCabinClass(
                request.getFlightId(), request.getCabinClass())) {
            throw new ResourceAlreadyExistsException(
                    "A fare for cabin class " + request.getCabinClass()
                            + " already exists for Flight ID " + request.getFlightId() + ".");
        }

        FlightFare fare = flightFareMapper.toEntity(request);
        fare.setFlight(flight);

        FlightFare savedFare = flightFareRepository.save(fare);

        return flightFareMapper.toDto(savedFare);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightFareResDTO> getFaresByFlightId(Long flightId) {

        return flightFareRepository.findByFlightId(flightId)
                .stream()
                .map(flightFareMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FlightFareResDTO getFareByFlightIdAndCabinClass(Long flightId, CabinClass cabinClass) {

        FlightFare fare = flightFareRepository.findByFlightIdAndCabinClass(flightId, cabinClass)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No fare found for Flight ID " + flightId
                                + " and cabin class " + cabinClass + "."));

        return flightFareMapper.toDto(fare);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightFareResDTO> getAllFares() {

        return flightFareRepository.findAll()
                .stream()
                .map(flightFareMapper::toDto)
                .toList();
    }

    @Override
    public FlightFareResDTO updateFare(
            Long flightId,
            CabinClass cabinClass,
            FlightFareUpdateReqDTO request) {

        FlightFare fare = flightFareRepository
                .findByFlightIdAndCabinClass(flightId, cabinClass)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No fare found for Flight ID " + flightId
                                + " and cabin class " + cabinClass + "."));

        fare.setPrice(request.getPrice());
        fare.setCurrency(request.getCurrency());
        fare.setIncludesTax(request.getIncludesTax());

        FlightFare updatedFare = flightFareRepository.save(fare);

        return flightFareMapper.toDto(updatedFare);
    }

    @Override
    public void deleteFare(Long id) {

        FlightFare fare = flightFareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fare with ID " + id + " not found."));

        flightFareRepository.delete(fare);
    }
}