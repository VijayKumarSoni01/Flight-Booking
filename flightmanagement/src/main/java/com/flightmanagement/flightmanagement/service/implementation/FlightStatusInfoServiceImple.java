package com.flightmanagement.flightmanagement.service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightStatusInfoReqDTO;
// import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightStatusInfoUpdateReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightStatusInfoResDTO;
import com.flightmanagement.flightmanagement.entity.Flight;
import com.flightmanagement.flightmanagement.entity.FlightStatusInfo;
import com.flightmanagement.flightmanagement.enums.FlightStatus;
import com.flightmanagement.flightmanagement.exception.ResourceAlreadyExistsException;
import com.flightmanagement.flightmanagement.exception.ResourceNotFoundException;
import com.flightmanagement.flightmanagement.mapper.FlightStatusInfoMapper;
import com.flightmanagement.flightmanagement.repository.FlightRepository;
import com.flightmanagement.flightmanagement.repository.FlightStatusInfoRepository;
import com.flightmanagement.flightmanagement.service.interFace.FlightStatusInfoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightStatusInfoServiceImple implements FlightStatusInfoService {

        private final FlightStatusInfoRepository flightStatusInfoRepository;
        private final FlightRepository flightRepository;
        private final FlightStatusInfoMapper flightStatusInfoMapper;

        @Override
        public FlightStatusInfoResDTO createFlightStatus(FlightStatusInfoReqDTO request) {

                Flight flight = flightRepository.findById(request.getFlightId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Flight with ID " + request.getFlightId() + " not found."));

                if (flightStatusInfoRepository.existsByFlightId(request.getFlightId())) {
                        throw new ResourceAlreadyExistsException(
                                        "Status information already exists for Flight ID "
                                                        + request.getFlightId() + ".");
                }

                FlightStatusInfo statusInfo = flightStatusInfoMapper.toEntity(request);

                statusInfo.setFlight(flight);
                statusInfo.setLastApiSync(LocalDateTime.now());

                FlightStatusInfo saved = flightStatusInfoRepository.save(statusInfo);

                return flightStatusInfoMapper.toDto(saved);
        }

        @Override
        @Transactional(readOnly = true)
        public FlightStatusInfoResDTO getFlightStatusByFlightId(Long flightId) {

                FlightStatusInfo statusInfo = flightStatusInfoRepository
                                .findByFlightId(flightId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "No status information found for Flight ID "
                                                                + flightId + "."));

                return flightStatusInfoMapper.toDto(statusInfo);
        }

        @Override
        @Transactional(readOnly = true)
        public List<FlightStatusInfoResDTO> getAllFlightStatuses() {

                return flightStatusInfoRepository.findAll()
                                .stream()
                                .map(flightStatusInfoMapper::toDto)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<FlightStatusInfoResDTO> getFlightStatusesByStatus(
                        FlightStatus status) {

                return flightStatusInfoRepository.findByStatus(status)
                                .stream()
                                .map(flightStatusInfoMapper::toDto)
                                .toList();
        }

        @Override
        public FlightStatusInfoResDTO updateFlightStatus(
                        Long flightId,
                        FlightStatusInfoReqDTO request) {

                FlightStatusInfo statusInfo = flightStatusInfoRepository
                                .findByFlightId(flightId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "No status information found for Flight ID "
                                                                + flightId + "."));

                if (!flightId.equals(request.getFlightId())) {
                        throw new IllegalArgumentException(
                                        "Flight ID in URL and request body must match.");
                }

                flightStatusInfoMapper.updateEntityFromDto(request, statusInfo);

                statusInfo.setLastApiSync(LocalDateTime.now());

                FlightStatusInfo updated = flightStatusInfoRepository.save(statusInfo);

                return flightStatusInfoMapper.toDto(updated);
        }

        @Override
        public void deleteFlightStatus(Long flightId) {

                FlightStatusInfo statusInfo = flightStatusInfoRepository
                                .findByFlightId(flightId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "No status information found for Flight ID "
                                                                + flightId + "."));

                flightStatusInfoRepository.delete(statusInfo);
        }
}