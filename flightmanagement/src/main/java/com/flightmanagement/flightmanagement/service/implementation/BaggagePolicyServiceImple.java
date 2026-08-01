package com.flightmanagement.flightmanagement.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightmanagement.flightmanagement.service.interFace.BaggagePolicyService;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.BaggagePolicyReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.BaggagePolicyResDTO;
import com.flightmanagement.flightmanagement.entity.Aircraft;
import com.flightmanagement.flightmanagement.entity.BaggagePolicy;
import com.flightmanagement.flightmanagement.entity.Flight;
import com.flightmanagement.flightmanagement.enums.CabinClass;
import com.flightmanagement.flightmanagement.mapper.BaggagePolicyMapper;
import com.flightmanagement.flightmanagement.repository.BaggageRepository;
import com.flightmanagement.flightmanagement.repository.FlightRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BaggagePolicyServiceImple implements BaggagePolicyService {

        private final BaggageRepository baggagePolicyRepository;
        private final BaggagePolicyMapper baggagePolicyMapper;
        private final FlightRepository flightRepository;

        @Override
        @Transactional
        public BaggagePolicyResDTO createBaggagePolicy(BaggagePolicyReqDTO request) {

                Flight flight = flightRepository.findById(request.getFlightId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Flight with ID "
                                                                + request.getFlightId()
                                                                + " not found."));

                Aircraft aircraft = flight.getAircraft();

                if (!aircraft.isCabinAvailable(request.getCabinClass())) {
                        throw new IllegalArgumentException(
                                        request.getCabinClass()
                                                        + " cabin is not available for aircraft "
                                                        + aircraft.getModel());
                }

                if (baggagePolicyRepository.existsByFlightIdAndCabinClass(
                                request.getFlightId(),
                                request.getCabinClass())) {

                        throw new IllegalArgumentException(
                                        "Baggage policy already exists for Flight ID "
                                                        + request.getFlightId()
                                                        + " and Cabin Class "
                                                        + request.getCabinClass());
                }

                BaggagePolicy policy = baggagePolicyMapper.toEntity(request);

                policy.setFlight(flight);
                policy.setCabinClass(request.getCabinClass());

                BaggagePolicy savedPolicy = baggagePolicyRepository.save(policy);

                return baggagePolicyMapper.toDto(savedPolicy);
        }

        @Override
        @Transactional(readOnly = true)
        public BaggagePolicyResDTO getPolicyByFlightAndCabin(
                        Long flightId,
                        CabinClass cabinClass) {

                BaggagePolicy policy = baggagePolicyRepository
                                .findByFlightIdAndCabinClass(flightId, cabinClass)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Baggage policy not found."));

                return baggagePolicyMapper.toDto(policy);
        }

        @Override
        @Transactional(readOnly = true)
        public List<BaggagePolicyResDTO> getAllBaggagePolicies() {

                return baggagePolicyRepository.findAll()
                                .stream()
                                .map(baggagePolicyMapper::toDto)
                                .toList();
        }

        @Override
        @Transactional
        public BaggagePolicyResDTO updateBaggagePolicy(
                        Long id,
                        BaggagePolicyReqDTO request) {

                BaggagePolicy policy = getBaggagePolicy(id);

                Flight flight = flightRepository.findById(request.getFlightId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Flight with ID "
                                                                + request.getFlightId()
                                                                + " not found."));

                if (!policy.getFlight().getId().equals(request.getFlightId())
                                || policy.getCabinClass() != request.getCabinClass()) {

                        if (baggagePolicyRepository.existsByFlightIdAndCabinClass(
                                        request.getFlightId(),
                                        request.getCabinClass())) {

                                throw new IllegalArgumentException(
                                                "Baggage policy already exists for this flight and cabin class.");
                        }
                }

                policy.setFlight(flight);
                policy.setCabinClass(request.getCabinClass());

                baggagePolicyMapper.updateEntityFromDto(request, policy);

                return baggagePolicyMapper.toDto(
                                baggagePolicyRepository.save(policy));
        }

        @Override
        @Transactional
        public void deleteBaggagePolicy(Long id) {

                baggagePolicyRepository.delete(getBaggagePolicy(id));
        }

        private BaggagePolicy getBaggagePolicy(Long id) {

                return baggagePolicyRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Baggage policy with ID " + id + " not found."));
        }
}