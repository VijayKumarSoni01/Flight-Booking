package com.flightmanagement.flightmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightmanagement.flightmanagement.entity.BaggagePolicy;
import com.flightmanagement.flightmanagement.enums.CabinClass;

public interface BaggageRepository 
        extends JpaRepository<BaggagePolicy, Long> {


    boolean existsByFlightId(Long flightId);


    List<BaggagePolicy> findByFlightId(Long flightId);


    Optional<BaggagePolicy> findByFlightIdAndCabinClass(
            Long flightId,
            CabinClass cabinClass
    );


    boolean existsByFlightIdAndCabinClass(
            Long flightId,
            CabinClass cabinClass
    );

}