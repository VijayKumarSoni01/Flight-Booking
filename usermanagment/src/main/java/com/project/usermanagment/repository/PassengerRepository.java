package com.project.usermanagment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.usermanagment.entity.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    List<Passenger> findByUserId(Long userId);

}
