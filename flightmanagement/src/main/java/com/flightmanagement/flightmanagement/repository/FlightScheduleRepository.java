package com.flightmanagement.flightmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightmanagement.flightmanagement.entity.FlightSchedule;
import com.flightmanagement.flightmanagement.enums.FlightStatus;

public interface FlightScheduleRepository
        extends JpaRepository<FlightSchedule, Long> {

    List<FlightSchedule> findByStatus(
            FlightStatus status
    );

    List<FlightSchedule> findByFlightId(
            Long flightId
    );

    boolean existsByFlightIdAndStatus(
            Long flightId,
            FlightStatus status
    );
}