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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/private/passangers")
@RequiredArgsConstructor
public class PassangerController {

        private final PassengerService service;

        @PostMapping
        public ResponseEntity<ApiResponse<PassengerResponseDTO>> addPassanger(
                        @Valid @RequestBody PassengerRequestDTO request, Authentication auth) {
                String email = auth.getName();

                PassengerResponseDTO res = service.addPassanger(email, request);

                return ResponseEntity.ok(ApiResponse.success(res, "Passanger added successfully"));
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<PassengerResponseDTO>>> getPassangers(
                        Authentication auth) {
                String email = auth.getName();

                return ResponseEntity.ok(
                                ApiResponse.<List<PassengerResponseDTO>>success(
                                                service.getMyPassengers(email),
                                                "Passengers fetched"));
        }

        @PatchMapping("/{id}")
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
        public ResponseEntity<ApiResponse<String>> deletePassenger(
                        @PathVariable Long id, Authentication auth) {
                service.deletePassenger(id, auth.getName());

                return ResponseEntity.ok(ApiResponse.success(null, "Passenger delete successfully"));
        }
}
