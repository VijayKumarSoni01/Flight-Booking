package com.flightmanagement.flightmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightFareReqDTO;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightFareUpdateReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightFareResDTO;
import com.flightmanagement.flightmanagement.enums.CabinClass;
import com.flightmanagement.flightmanagement.service.interFace.FlightFareService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flight-fares")
@RequiredArgsConstructor
@Tag(name = "Flight Fare Management", description = "Manage flight fares for different cabin classes.")
public class FlightFareController {

    private final FlightFareService flightFareService;

    @PostMapping
    @Operation(summary = "Create Flight Fare", description = "Create a new fare for a specific flight and cabin class.")
    public ResponseEntity<FlightFareResDTO> createFare(
            @Valid @RequestBody FlightFareReqDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flightFareService.createFare(request));
    }

    @GetMapping("/flight/{flightId}")
    @Operation(summary = "Get All Fares by Flight ID", description = "Returns all cabin fares (Economy, Premium Economy, Business and First) for the specified flight.")
    public ResponseEntity<List<FlightFareResDTO>> getFaresByFlightId(
            @Parameter(description = "Flight ID", example = "1") @PathVariable Long flightId) {

        return ResponseEntity.ok(flightFareService.getFaresByFlightId(flightId));
    }

    @GetMapping("/flight/{flightId}/class/{cabinClass}")
    @Operation(summary = "Get Fare by Flight ID and Cabin Class", description = "Returns the fare for a specific cabin class of a flight.")
    public ResponseEntity<FlightFareResDTO> getFareByFlightIdAndCabinClass(

            @Parameter(description = "Flight ID", example = "1") @PathVariable Long flightId,

            @Parameter(description = "Cabin Class", example = "ECONOMY") @PathVariable CabinClass cabinClass) {

        return ResponseEntity.ok(
                flightFareService.getFareByFlightIdAndCabinClass(
                        flightId, cabinClass));
    }

    @GetMapping
    @Operation(summary = "Get All Flight Fares", description = "Returns every fare record available in the system.")
    public ResponseEntity<List<FlightFareResDTO>> getAllFares() {

        return ResponseEntity.ok(flightFareService.getAllFares());
    }

    @PutMapping("/flight/{flightId}/class/{cabinClass}")
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update Flight Fare", description = "Update an existing fare by Fare ID.")
    public ResponseEntity<FlightFareResDTO> updateFare(
            @PathVariable Long flightId,
            @PathVariable CabinClass cabinClass,
            @Valid @RequestBody FlightFareUpdateReqDTO request) {

        return ResponseEntity.ok(
                flightFareService.updateFare(flightId, cabinClass, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Flight Fare", description = "Delete a fare using its Fare ID.")
    public ResponseEntity<Void> deleteFare(

            @Parameter(description = "Flight Fare ID", example = "1") @PathVariable Long id) {

        flightFareService.deleteFare(id);
        return ResponseEntity.noContent().build();
    }
}