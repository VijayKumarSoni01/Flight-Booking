package com.flightmanagement.flightmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightStatusInfoReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightStatusInfoResDTO;
import com.flightmanagement.flightmanagement.enums.FlightStatus;
import com.flightmanagement.flightmanagement.service.interFace.FlightStatusInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flight-status-info")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Flight Status Information", description = "Manage real-time flight operational status, delays, gates, and timings.")
public class FlightStatusInfoController {

    private final FlightStatusInfoService flightStatusInfoService;

    @PostMapping
    @Operation(summary = "Create Flight Status Information", description = "Creates operational status information for a flight. Only one status record is allowed per flight.")
    public ResponseEntity<FlightStatusInfoResDTO> createFlightStatus(
            @Valid @RequestBody FlightStatusInfoReqDTO request) {

        FlightStatusInfoResDTO response = flightStatusInfoService.createFlightStatus(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/flight/{flightId}")
    @Operation(summary = "Get Flight Status by Flight ID", description = "Retrieves the operational status information of a specific flight.")
    public ResponseEntity<FlightStatusInfoResDTO> getFlightStatusByFlightId(
            @Parameter(description = "Flight ID", example = "1") @PathVariable Long flightId) {

        FlightStatusInfoResDTO response = flightStatusInfoService.getFlightStatusByFlightId(flightId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get All Flight Status Records", description = "Retrieves operational status information for all flights.")
    public ResponseEntity<List<FlightStatusInfoResDTO>> getAllFlightStatuses() {

        List<FlightStatusInfoResDTO> response = flightStatusInfoService.getAllFlightStatuses();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get Flights by Status", description = "Retrieves all flights having the specified operational status.")
    public ResponseEntity<List<FlightStatusInfoResDTO>> getFlightStatusesByStatus(
            @Parameter(description = "Flight Status", example = "BOARDING") @PathVariable FlightStatus status) {

        List<FlightStatusInfoResDTO> response = flightStatusInfoService.getFlightStatusesByStatus(status);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/flight/{flightId}")
    @Operation(summary = "Update Flight Status Information", description = "Updates the operational status, estimated/actual timings, delay information, and gates for a flight.")
    public ResponseEntity<FlightStatusInfoResDTO> updateFlightStatus(
            @PathVariable Long flightId,
            @Valid @RequestBody FlightStatusInfoReqDTO request) {

        return ResponseEntity.ok(
                flightStatusInfoService.updateFlightStatus(flightId, request));
    }

    @DeleteMapping("/flight/{flightId}")
    @Operation(summary = "Delete Flight Status Information", description = "Deletes the operational status record associated with the specified flight.")
    public ResponseEntity<Void> deleteFlightStatus(
            @Parameter(description = "Flight ID", example = "1") @PathVariable Long flightId) {

        flightStatusInfoService.deleteFlightStatus(flightId);

        return ResponseEntity.noContent().build();
    }
}