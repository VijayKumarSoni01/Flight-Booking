package com.flightmanagement.flightmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightmanagement.flightmanagement.service.interFace.FlightInstanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/flight-instances")
@RequiredArgsConstructor
public class FlightInstanceController {

    private final FlightInstanceService flightInstanceService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateInstances() {

        flightInstanceService.generateFutureInstances();

        return ResponseEntity.ok(
                "Future flight instances generated successfully"
        );
    }

    @PostMapping("/generate-missing-seats")
    public ResponseEntity<String> generateMissingInstanceSeats() {

        flightInstanceService.generateMissingInstanceSeats();

        return ResponseEntity.ok(
                "Missing flight instance seats generated successfully"
        );
    }
}