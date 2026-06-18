package com.project.usermanagment.controller.PassangerController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.usermanagment.dtos.PassengerDTO.PassengerRequestDTO;
import com.project.usermanagment.dtos.PassengerDTO.PassengerResponseDTO;
import com.project.usermanagment.dtos.PassengerDTO.UpdatePassengerDTO;
import com.project.usermanagment.dtos.UserDTO.securitydto.ApiResponse;
import com.project.usermanagment.service.PassengerService.PassengerService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/private/passengers")
@RequiredArgsConstructor
public class PassengerController {

        private final PassengerService service;

        @PostMapping
        @Operation(summary = "Add Passenger", description = "Add a passenger to the logged-in user's account", tags = {
                        "Passenger APIs" })
        public ResponseEntity<ApiResponse<PassengerResponseDTO>> addPassenger(
                        @Valid @RequestBody PassengerRequestDTO request, Authentication auth) {
                String email = auth.getName();

                PassengerResponseDTO res = service.addPassenger(email, request);

                return ResponseEntity.ok(ApiResponse.success(res, "Passenger added successfully"));
        }

        @GetMapping
        @Operation(summary = "Get My Passengers", description = "Retrieve all passengers linked to the logged-in user", tags = {
                        "Passenger APIs" })
        public ResponseEntity<ApiResponse<List<PassengerResponseDTO>>> getPassengers(
                        Authentication auth) {
                String email = auth.getName();

                return ResponseEntity.ok(
                                ApiResponse.<List<PassengerResponseDTO>>success(
                                                service.getMyPassengers(email),
                                                "Passengers fetched"));
        }

        @PatchMapping("/{id}")
        @Operation(summary = "Update Passenger", description = "Update passenger details by passenger ID", tags = {
                        "Passenger APIs" })
        public ResponseEntity<ApiResponse<PassengerResponseDTO>> updatePassenger(
                        @PathVariable Long id,
                        @RequestBody UpdatePassengerDTO req,
                        Authentication auth) {

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                service.updatePassenger(id, auth.getName(), req),
                                                "Passenger updated successfully"));
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete Passenger", description = "Delete passenger by passenger ID", tags = {
                        "Passenger APIs" })
        public ResponseEntity<ApiResponse<String>> deletePassenger(
                        @PathVariable Long id, Authentication auth) {
                service.deletePassenger(id, auth.getName());

                return ResponseEntity.ok(ApiResponse.success(null, "Passenger delete successfully"));
        }
}
