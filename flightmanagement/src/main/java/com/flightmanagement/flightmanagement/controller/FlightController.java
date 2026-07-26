package com.flightmanagement.flightmanagement.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flightmanagement.flightmanagement.service.interFace.FlightService;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightResDTO;
import com.flightmanagement.flightmanagement.enums.FlightStatus;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flights")
// @PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<FlightResDTO> createFlight(
            @Valid @RequestBody FlightReqDTO request) {

        FlightResDTO response = flightService.createFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResDTO> getFlightById(@PathVariable Long id) {

        FlightResDTO response = flightService.getFlightById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FlightResDTO>> getAllFlights() {

        List<FlightResDTO> response = flightService.getAllFlights();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightResDTO>> searchFlights(
            @RequestParam Long originAirportId,
            @RequestParam Long destinationAirportId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate) {

        List<FlightResDTO> response = flightService.searchFlights(
                originAirportId, destinationAirportId, departureDate);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightResDTO> updateFlight(
            @PathVariable Long id,
            @Valid @RequestBody FlightReqDTO request) {

        FlightResDTO response = flightService.updateFlight(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FlightResDTO> updateFlightStatus(
            @PathVariable Long id,
            @RequestParam FlightStatus status) {

        FlightResDTO response = flightService.updateFlightStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {

        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}