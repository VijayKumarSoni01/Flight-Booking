package com.flightmanagement.flightmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightmanagement.flightmanagement.entity.Airport;

public interface AirportRepository extends JpaRepository<Airport, Long> {

    boolean existsByIataCode(String iataCode);

    boolean existsByIcaoCode(String icaoCode);

    boolean existsByNameAndCity(String name, String city);

    boolean existsByIataCodeAndIdNot(String iataCode, Long id);

    boolean existsByIcaoCodeAndIdNot(String icaoCode, Long id);

    List<Airport> findByActiveTrue();
}
