package com.flightmanagement.flightmanagement.controller.publicFlight;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flightmanagement.flightmanagement.dtos.responseDTOs.PublicFlightResDTO;
import com.flightmanagement.flightmanagement.service.interFace.publicService.PublicFlightService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/flights")
@RequiredArgsConstructor
public class PublicFlightController {

        private final PublicFlightService publicFlightService;

        @GetMapping("/search")
        public ResponseEntity<List<PublicFlightResDTO>> searchFlights(

                        @RequestParam String source,

                        @RequestParam String destination,

                        @RequestParam LocalDate date

        ) {

                return ResponseEntity.ok(

                                publicFlightService.searchFlights(
                                                source,
                                                destination,
                                                date)

                );

        }

        @GetMapping("/{flightId}")
        public ResponseEntity<PublicFlightResDTO> getFlight(

                        @PathVariable Long flightId

        ) {

                return ResponseEntity.ok(

                                publicFlightService.getFlightDetails(
                                                flightId)

                );

        }

}