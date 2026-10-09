package com.flightmanagement.flightmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.FlightScheduleRequest;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightScheduleResponseDTO;
import com.flightmanagement.flightmanagement.service.interFace.FlightScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/flight-schedules")
@RequiredArgsConstructor
public class FlightScheduleController {

        private final FlightScheduleService flightScheduleService;

        @PostMapping
        public ResponseEntity<FlightScheduleResponseDTO> createSchedule(
                        @RequestBody FlightScheduleRequest request) {

                FlightScheduleResponseDTO schedule = flightScheduleService.createSchedule(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(schedule);
        }

        @GetMapping
        public ResponseEntity<List<FlightScheduleResponseDTO>> getAllSchedules() {

                return ResponseEntity.ok(
                                flightScheduleService.getAllSchedules());
        }

        @GetMapping("/{scheduleId}")
        public ResponseEntity<FlightScheduleResponseDTO> getScheduleById(
                        @PathVariable Long scheduleId) {

                return ResponseEntity.ok(
                                flightScheduleService.getScheduleById(
                                                scheduleId));
        }

        @GetMapping("/flight/{flightId}")
        public ResponseEntity<List<FlightScheduleResponseDTO>> getSchedulesByFlight(
                        @PathVariable Long flightId) {

                return ResponseEntity.ok(
                                flightScheduleService.getSchedulesByFlight(
                                                flightId));
        }

        @DeleteMapping("/{scheduleId}")
        public ResponseEntity<Void> deleteSchedule(
                        @PathVariable Long scheduleId) {

                flightScheduleService.deleteSchedule(
                                scheduleId);

                return ResponseEntity
                                .noContent()
                                .build();
        }
}