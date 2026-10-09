package com.flightmanagement.flightmanagement.controller.publicFlight;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatResDTO;
import com.flightmanagement.flightmanagement.service.interFace.SeatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<SeatResDTO>> getSeatsByFlight(
            @PathVariable Long flightId) {

        return ResponseEntity.ok(
                seatService.getSeatsByFlight(flightId)
        );
    }

    @GetMapping("/booking/{bookingReference}")
    public ResponseEntity<List<SeatResDTO>> getSeatsByBookingReference(
            @PathVariable String bookingReference) {

        return ResponseEntity.ok(
                seatService.getSeatsByBookingReference(
                        bookingReference)
        );
    }
}