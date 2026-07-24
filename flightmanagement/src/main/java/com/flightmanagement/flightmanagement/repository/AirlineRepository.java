package com.flightmanagement.flightmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightmanagement.flightmanagement.entity.Airline;

public interface AirlineRepository extends JpaRepository<Airline, Long>{

    boolean existsByName(String name);

    boolean existsByIataCode(String iataCode);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByIataCodeAndIdNot(String iataCode, Long id);

    List<Airline> findByActiveTrue();
        
    }
