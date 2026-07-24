package com.flightmanagement.flightmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightmanagement.flightmanagement.entity.Aircraft;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    boolean existsByRegistrationNumber(String registrationNumber);

}