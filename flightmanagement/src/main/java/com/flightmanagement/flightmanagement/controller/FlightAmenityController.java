package com.flightmanagement.flightmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightAmenityReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightAmenityResDTO;
import com.flightmanagement.flightmanagement.service.interFace.FlightAmenityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flight-amenities")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class FlightAmenityController {

    private final FlightAmenityService flightAmenityService;

    @PostMapping
    public ResponseEntity<FlightAmenityResDTO> createAmenity(
            @Valid @RequestBody FlightAmenityReqDTO request) {

        FlightAmenityResDTO response = flightAmenityService.createAmenity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightAmenityResDTO> getAmenityById(@PathVariable Long id) {

        FlightAmenityResDTO response = flightAmenityService.getAmenityById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<FlightAmenityResDTO> getAmenityByFlightId(@PathVariable Long flightId) {

        FlightAmenityResDTO response = flightAmenityService.getAmenityByFlightId(flightId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FlightAmenityResDTO>> getAllAmenities() {

        List<FlightAmenityResDTO> response = flightAmenityService.getAllAmenities();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightAmenityResDTO> updateAmenity(
            @PathVariable Long id,
            @Valid @RequestBody FlightAmenityReqDTO request) {

        FlightAmenityResDTO response = flightAmenityService.updateAmenity(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {

        flightAmenityService.deleteAmenity(id);
        return ResponseEntity.noContent().build();
    }
}