package com.flightmanagement.flightmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightmanagement.flightmanagement.service.interFace.AirlineService;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.AirlineReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.AirlineResDTO;
import com.flightmanagement.flightmanagement.dtos.securityDTOs.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/airlines")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Validated
public class AirlineController {

    private final AirlineService airlineService;

    @PostMapping
    public ResponseEntity<ApiResponse<AirlineResDTO>> createAirline(
            @Valid @RequestBody AirlineReqDTO request) {

        AirlineResDTO response = airlineService.createAirline(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Airline created successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AirlineResDTO>> getAirlineById(
            @PathVariable Long id) {

        AirlineResDTO response = airlineService.getAirlineById(id);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Airline fetched successfully."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AirlineResDTO>>> getAllAirlines() {

        List<AirlineResDTO> response = airlineService.getAllAirlines();

        return ResponseEntity.ok(
                ApiResponse.success(response, "Airlines fetched successfully."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AirlineResDTO>> updateAirline(
            @PathVariable Long id,
            @Valid @RequestBody AirlineReqDTO request) {

        AirlineResDTO response = airlineService.updateAirline(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Airline updated successfully."));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateAirline(
            @PathVariable Long id) {

        airlineService.deactivateAirline(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Airline deactivated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAirline( 
            @PathVariable Long id) {

        airlineService.deleteAirline(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Airline deleted successfully."));
    }

}
