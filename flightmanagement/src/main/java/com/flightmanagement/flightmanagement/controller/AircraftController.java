package com.flightmanagement.flightmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightmanagement.flightmanagement.service.interFace.AircraftService;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.AircraftReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.AircraftResDTO;
import com.flightmanagement.flightmanagement.dtos.securityDTOs.ApiResponse;
import com.flightmanagement.flightmanagement.enums.CabinClass;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/aircrafts")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Validated
public class AircraftController {

    private final AircraftService aircraftService;

    @PostMapping
    public ResponseEntity<ApiResponse<AircraftResDTO>> createAircraft(
            @Valid @RequestBody AircraftReqDTO request) {

        AircraftResDTO response = aircraftService.createAircraft(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Aircraft created successfully."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AircraftResDTO>>> getAllAircraft() {

        List<AircraftResDTO> response = aircraftService.getAllAircraft();

        return ResponseEntity.ok(ApiResponse.success(response, "Aircraft retrieved successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AircraftResDTO>> getAircraftById(
            @PathVariable Long id) {

        AircraftResDTO response = aircraftService.getAircraftById(id);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Aircraft retrieved successfully."));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<AircraftResDTO>> updateAircraft(
            @PathVariable Long id,
            @Valid @RequestBody AircraftReqDTO request) {

        AircraftResDTO response = aircraftService.updateAircraft(id, request);

        return ResponseEntity.ok(ApiResponse.success(response, "Aircraft updated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAircraft(@PathVariable Long id) {

        aircraftService.deleteAircraft(id);

        return ResponseEntity.ok(ApiResponse.success(null, "Aircraft deleted successfully."));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateAircraft(
            @PathVariable Long id) {

        aircraftService.deactivateAircraft(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Aircraft deactivated successfully."));
    }

    @GetMapping("/{id}/cabins/{cabinClass}")
    public ResponseEntity<ApiResponse<Boolean>> isCabinAvailable(
            @PathVariable Long id,
            @PathVariable CabinClass cabinClass) {

        boolean available = aircraftService.isCabinAvailable(id, cabinClass);

        return ResponseEntity.ok(
                ApiResponse.success(
                        available,
                        "Cabin availability checked successfully."));
    }

}
