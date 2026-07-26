package com.flightmanagement.flightmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightmanagement.flightmanagement.service.interFace.AirportServices;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.AirportReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.AirportResDTO;
import com.flightmanagement.flightmanagement.dtos.securityDTOs.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/airports")
// @PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Validated
public class AirportController {

    private final AirportServices airportServices;

    @PostMapping
    public ResponseEntity<ApiResponse<AirportResDTO>> createAirport(
            @Valid @RequestBody AirportReqDTO request) {

        AirportResDTO response = airportServices.createAirport(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Airport created successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResDTO>> getAirportById(
            @PathVariable Long id) {

        AirportResDTO response = airportServices.getAirportById(id);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Airport fetched successfully."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AirportResDTO>>> getAllAirports() {

        List<AirportResDTO> response = airportServices.getAllAirports();

        return ResponseEntity.ok(
                ApiResponse.success(response, "Airports fetched successfully."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResDTO>> updateAirport(
            @PathVariable Long id,
            @Valid @RequestBody AirportReqDTO request) {

        AirportResDTO response = airportServices.updateAirport(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Airport updated successfully."));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateAirport(
            @PathVariable Long id) {

        airportServices.deactivateAirport(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Airport deactivated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAirport(@PathVariable Long id) {
        airportServices.deleteAirport(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Airport deleted successfully."));
    }
}