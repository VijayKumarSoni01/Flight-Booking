package com.flightmanagement.flightmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightmanagement.flightmanagement.entity.FlightStatusInfo;
import com.flightmanagement.flightmanagement.enums.FlightStatus;

public interface FlightStatusInfoRepository extends JpaRepository<FlightStatusInfo, Long> {

    Optional<FlightStatusInfo> findByFlightId(Long flightId);

    boolean existsByFlightId(Long flightId);

    List<FlightStatusInfo> findByStatus(FlightStatus status);
}