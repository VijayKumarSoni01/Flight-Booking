package com.flightmanagement.flightmanagement.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightmanagement.flightmanagement.entity.FlightInstance;
import com.flightmanagement.flightmanagement.entity.FlightInstanceSeat;
import com.flightmanagement.flightmanagement.entity.FlightSchedule;
import com.flightmanagement.flightmanagement.entity.Seat;
import com.flightmanagement.flightmanagement.enums.FlightStatus;
import com.flightmanagement.flightmanagement.enums.FrequencyType;
import com.flightmanagement.flightmanagement.enums.SeatStatus;
import com.flightmanagement.flightmanagement.repository.FlightInstanceRepository;
import com.flightmanagement.flightmanagement.repository.FlightInstanceSeatRepository;
import com.flightmanagement.flightmanagement.repository.FlightScheduleRepository;
import com.flightmanagement.flightmanagement.repository.SeatRepository;
import com.flightmanagement.flightmanagement.service.interFace.FlightInstanceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightInstanceServiceImpl
        implements FlightInstanceService {

    private static final int GENERATION_DAYS = 30;

    private final FlightScheduleRepository flightScheduleRepository;

    private final FlightInstanceRepository flightInstanceRepository;

    private final SeatRepository seatRepository;

    private final FlightInstanceSeatRepository flightInstanceSeatRepository;

    @Override
    @Transactional
    public void generateFutureInstances() {

        LocalDate startDate = LocalDate.now().plusDays(1);

        LocalDate endDate = startDate.plusDays(GENERATION_DAYS - 1);

        List<FlightSchedule> schedules = flightScheduleRepository.findByStatus(
                FlightStatus.SCHEDULED);

        for (FlightSchedule schedule : schedules) {

            generateInstances(
                    schedule,
                    startDate,
                    endDate);
        }
    }

    private void generateInstances(
            FlightSchedule schedule,
            LocalDate startDate,
            LocalDate endDate) {

        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {

            if (shouldOperate(schedule, date)) {

                createInstanceIfMissing(
                        schedule,
                        date);
            }

            date = date.plusDays(1);
        }
    }

    private void createInstanceIfMissing(
            FlightSchedule schedule,
            LocalDate date) {

        boolean exists = flightInstanceRepository
                .existsByScheduleIdAndFlightDate(
                        schedule.getId(),
                        date);

        if (exists) {
            return;
        }

        LocalDateTime departure = LocalDateTime.of(
                date,
                schedule.getDepartureTime());

        LocalDateTime arrival = LocalDateTime.of(
                date,
                schedule.getArrivalTime());

        FlightInstance instance = FlightInstance.builder()
                .schedule(schedule)
                .flightDate(date)
                .departureTime(departure)
                .arrivalTime(arrival)
                .status(schedule.getStatus())
                .build();

        FlightInstance savedInstance = flightInstanceRepository.save(instance);

        createInstanceSeats(savedInstance);
    }

    private void createInstanceSeats(
            FlightInstance flightInstance) {

        Long flightId = flightInstance
                .getSchedule()
                .getFlight()
                .getId();

        List<Seat> seats = seatRepository.findByFlightId(
                flightId);

        if (seats.isEmpty()) {

            return;
        }

        List<FlightInstanceSeat> instanceSeats = seats.stream()
                .map(seat -> FlightInstanceSeat.builder()
                        .flightInstance(
                                flightInstance)
                        .seat(seat)
                        .seatStatus(
                                SeatStatus.AVAILABLE)
                        .bookingReference(null)
                        .reservedAt(null)
                        .build())
                .toList();

        flightInstanceSeatRepository.saveAll(
                instanceSeats);
    }

    @Override
    @Transactional
    public void generateMissingInstanceSeats() {

        List<FlightInstance> instances = flightInstanceRepository.findAll();

        int createdInstances = 0;
        int createdSeats = 0;

        for (FlightInstance instance : instances) {

            boolean exists = flightInstanceSeatRepository
                    .existsByFlightInstanceId(instance.getId());

            if (exists) {
                continue;
            }

            Long flightId = instance
                    .getSchedule()
                    .getFlight()
                    .getId();

            List<Seat> seats = seatRepository.findByFlightId(flightId);

            if (seats.isEmpty()) {
                continue;
            }

            List<FlightInstanceSeat> instanceSeats = seats.stream()
                    .map(seat -> FlightInstanceSeat.builder()
                            .flightInstance(instance)
                            .seat(seat)
                            .seatStatus(SeatStatus.AVAILABLE)
                            .bookingReference(null)
                            .reservedAt(null)
                            .build())
                    .toList();

            flightInstanceSeatRepository.saveAll(instanceSeats);

            createdInstances++;
            createdSeats += instanceSeats.size();
        }

        System.out.println(
                "Created seats for " + createdInstances
                        + " flight instances. Total instance seats = "
                        + createdSeats);
    }

    private boolean shouldOperate(
            FlightSchedule schedule,
            LocalDate date) {

        FrequencyType frequency = schedule.getFrequency();

        if (frequency == null) {
            return false;
        }

        return switch (frequency) {

            case DAILY ->
                true;

            case WEEKEND ->
                date.getDayOfWeek().getValue() >= 6;

            case MONDAY_TO_FRIDAY ->
                date.getDayOfWeek().getValue() <= 5;

            case WEEKLY ->
                false;
        };
    }

}