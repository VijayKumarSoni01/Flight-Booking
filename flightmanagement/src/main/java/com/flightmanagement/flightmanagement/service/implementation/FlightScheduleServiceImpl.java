package com.flightmanagement.flightmanagement.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightScheduleRequest;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightScheduleResponseDTO;
import com.flightmanagement.flightmanagement.entity.Flight;
import com.flightmanagement.flightmanagement.entity.FlightSchedule;
import com.flightmanagement.flightmanagement.enums.FlightStatus;
import com.flightmanagement.flightmanagement.mapper.FlightScheduleMapper;
import com.flightmanagement.flightmanagement.repository.FlightRepository;
import com.flightmanagement.flightmanagement.repository.FlightScheduleRepository;
import com.flightmanagement.flightmanagement.service.interFace.FlightScheduleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightScheduleServiceImpl
                implements FlightScheduleService {

        private final FlightScheduleRepository flightScheduleRepository;
        private final FlightRepository flightRepository;
        private final FlightScheduleMapper flightScheduleMapper;

        @Override
        @Transactional
        public FlightScheduleResponseDTO createSchedule(
                        FlightScheduleRequest request) {

                Flight flight = flightRepository
                                .findById(request.getFlightId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Flight not found with ID: "
                                                                + request.getFlightId()));

                boolean alreadyScheduled = flightScheduleRepository
                                .existsByFlightIdAndStatus(
                                                flight.getId(),
                                                FlightStatus.SCHEDULED);

                if (alreadyScheduled) {
                        throw new RuntimeException(
                                        "Flight already has an active schedule.");
                }

                if (request.getDepartureTime() == null
                                || request.getArrivalTime() == null) {

                        throw new RuntimeException(
                                        "Departure time and arrival time are required.");
                }

                if (!request.getArrivalTime()
                                .isAfter(request.getDepartureTime())) {

                        throw new RuntimeException(
                                        "Arrival time must be after departure time.");
                }

                if (request.getFrequency() == null) {
                        throw new RuntimeException(
                                        "Frequency is required.");
                }

                FlightSchedule schedule = FlightSchedule.builder()
                                .flight(flight)
                                .flightNumber(flight.getFlightNumber())
                                .airline(flight.getAirline())
                                .originAirport(flight.getOriginAirport())
                                .destinationAirport(flight.getDestinationAirport())
                                .aircraft(flight.getAircraft())
                                .departureTime(request.getDepartureTime())
                                .arrivalTime(request.getArrivalTime())
                                .frequency(request.getFrequency())
                                .status(FlightStatus.SCHEDULED)
                                .build();

                FlightSchedule savedSchedule = flightScheduleRepository.save(schedule);

                return flightScheduleMapper.toDTO(savedSchedule);
        }

        @Override
        @Transactional(readOnly = true)
        public List<FlightScheduleResponseDTO> getAllSchedules() {

                return flightScheduleRepository.findAll()
                                .stream()
                                .map(flightScheduleMapper::toDTO)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<FlightScheduleResponseDTO> getSchedulesByFlight(
                        Long flightId) {

                return flightScheduleRepository
                                .findByFlightId(flightId)
                                .stream()
                                .map(flightScheduleMapper::toDTO)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public FlightScheduleResponseDTO getScheduleById(
                        Long scheduleId) {

                FlightSchedule schedule = flightScheduleRepository
                                .findById(scheduleId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Flight schedule not found with ID: "
                                                                + scheduleId));

                return flightScheduleMapper.toDTO(schedule);
        }

        @Override
        @Transactional
        public void deleteSchedule(Long scheduleId) {

                FlightSchedule schedule = flightScheduleRepository
                                .findById(scheduleId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Flight schedule not found with ID: "
                                                                + scheduleId));

                flightScheduleRepository.delete(schedule);
        }
}