package com.flightmanagement.flightmanagement.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.flightmanagement.flightmanagement.entity.Flight;
import com.flightmanagement.flightmanagement.enums.FlightStatus;
import com.flightmanagement.flightmanagement.repository.FlightRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FlightStatusScheduler {

    private final FlightRepository flightRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void autoOpenBoarding() {

        LocalDateTime now = LocalDateTime.now();

        List<Flight> boardingFlights =
                flightRepository.findByStatusAndDepartureTimeBetween(
                        FlightStatus.SCHEDULED,
                        now,
                        now.plusMinutes(30));

        boardingFlights.forEach(flight ->
                flight.setStatus(FlightStatus.BOARDING));

        flightRepository.saveAll(boardingFlights);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void autoMarkLanded() {

        LocalDateTime now = LocalDateTime.now();

        List<Flight> landedFlights =
                flightRepository.findByStatusAndArrivalTimeBefore(
                        FlightStatus.DEPARTED,
                        now);

        landedFlights.forEach(flight ->
                flight.setStatus(FlightStatus.LANDED));

        flightRepository.saveAll(landedFlights);
    }
}