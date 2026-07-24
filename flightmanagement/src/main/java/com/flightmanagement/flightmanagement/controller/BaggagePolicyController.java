package com.flightmanagement.flightmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightmanagement.flightmanagement.service.interFace.BaggagePolicyService;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.BaggagePolicyReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.BaggagePolicyResDTO;
import com.flightmanagement.flightmanagement.dtos.securityDTOs.ApiResponse;
import com.flightmanagement.flightmanagement.enums.CabinClass;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/baggage-policies")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Validated
public class BaggagePolicyController {

    private final BaggagePolicyService baggagePolicyService;

    @PostMapping()
    public ResponseEntity<ApiResponse<BaggagePolicyResDTO>> createBaggagePolicy(
            @Valid @RequestBody BaggagePolicyReqDTO request) {

        BaggagePolicyResDTO response = baggagePolicyService.createBaggagePolicy(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Baggage policy created successfully."));

    }

    @GetMapping("/flight/{flightId}/{cabinClass}")
    public ResponseEntity<ApiResponse<BaggagePolicyResDTO>> getBaggagePolicy(
            @PathVariable Long flightId,
            @PathVariable CabinClass cabinClass) {

        BaggagePolicyResDTO response = baggagePolicyService.getPolicyByFlightAndCabin(
                flightId,
                cabinClass);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Baggage policy retrieved successfully."));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<BaggagePolicyResDTO>>> getAllBaggagePolicies() {

        List<BaggagePolicyResDTO> response = baggagePolicyService.getAllBaggagePolicies();

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Baggage policy retrieved successfully."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BaggagePolicyResDTO>> updateBaggagePolicy(
            @PathVariable Long id,
            @Valid @RequestBody BaggagePolicyReqDTO request) {

        BaggagePolicyResDTO response = baggagePolicyService.updateBaggagePolicy(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Baggage policy updated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBaggagePolicy(@PathVariable Long id) {

        baggagePolicyService.deleteBaggagePolicy(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Baggage policy deleted successfully."));
    }
}
