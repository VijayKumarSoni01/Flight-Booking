package com.flightmanagement.flightmanagement.controller.booking;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.AutoSeatReservationReqDTO;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.SeatReservationReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightFareResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.FlightResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatAvailabilityResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatReservationResponse;
import com.flightmanagement.flightmanagement.enums.CabinClass;
import com.flightmanagement.flightmanagement.service.interFace.FlightFareService;
import com.flightmanagement.flightmanagement.service.interFace.FlightService;
import com.flightmanagement.flightmanagement.service.interFace.SeatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/private/flights")
@RequiredArgsConstructor
@Tag(name = "Flight Booking API", description = "APIs used by the Booking Service to validate flights, retrieve fares, manage seat availability, hold seats, confirm bookings, and release seats.")
public class FlightBookingController {

        private final FlightService flightService;
        private final FlightFareService flightFareService;
        private final SeatService seatService;

        @Operation(summary = "Get Flight Details", description = "Returns complete flight information for the specified Flight ID.")
        @GetMapping("/{flightId}")
        public ResponseEntity<FlightResDTO> getFlight(
                        @PathVariable Long flightId) {

                return ResponseEntity.ok(
                                flightService.getFlightById(flightId));
        }

        @Operation(summary = "Validate Flight", description = "Checks whether the flight exists and is available for booking.")
        @GetMapping("/{flightId}/validate")
        public ResponseEntity<Boolean> validateFlight(
                        @PathVariable Long flightId) {

                return ResponseEntity.ok(
                                flightService.validateFlight(flightId));
        }

        @Operation(summary = "Get Flight Fare", description = "Returns the fare for the specified cabin class of the selected flight.")
        @GetMapping("/{flightId}/fare/{cabinClass}")
        public ResponseEntity<FlightFareResDTO> getFare(
                        @PathVariable Long flightId,
                        @PathVariable CabinClass cabinClass) {

                return ResponseEntity.ok(
                                flightFareService.getFareByFlightIdAndCabinClass(
                                                flightId,
                                                cabinClass));
        }

        @Operation(summary = "Check Seat Availability", description = "Returns seat availability details including total, available, held, booked, and blocked seats for the selected cabin class.")
        @GetMapping("/{flightId}/seat-availability/{cabinClass}")
        public ResponseEntity<SeatAvailabilityResDTO> getSeatAvailability(
                        @PathVariable Long flightId,
                        @PathVariable CabinClass cabinClass) {

                return ResponseEntity.ok(
                                seatService.getSeatAvailability(
                                                flightId,
                                                cabinClass));
        }

        @Operation(summary = "Hold Seats", description = "Temporarily holds selected seats for a booking before payment is completed. Seats remain in HELD status until payment succeeds or the hold expires.")
        @PostMapping("/seats/hold")
        public ResponseEntity<List<String>> holdSeats(
                        @RequestBody SeatReservationReqDTO request) {

                return ResponseEntity.ok(
                                seatService.holdSeats(request));
        }

        @Operation(summary = "Confirm Seats", description = "Marks all HELD seats associated with the given booking reference as BOOKED after successful payment.")
        @PostMapping("/seats/confirm/{bookingReference}")
        public ResponseEntity<Void> confirmSeats(
                        @PathVariable String bookingReference) {

                seatService.confirmSeats(bookingReference);

                return ResponseEntity.ok().build();
        }

        @Operation(summary = "Release Seats", description = "Releases all HELD seats associated with the booking reference and makes them AVAILABLE again when payment fails, booking is cancelled, or the hold expires.")
        @PostMapping("/seats/release/{bookingReference}")
        public ResponseEntity<Void> releaseSeats(
                        @PathVariable String bookingReference) {

                System.out.println("Release API called for: " + bookingReference);

                seatService.releaseSeats(bookingReference);

                return ResponseEntity.ok().build();
        }

        @PostMapping("/{flightId}/reserve-seats")
        public ResponseEntity<SeatReservationResponse> reserveSeats(
                        @PathVariable Long flightId,
                        @Valid @RequestBody AutoSeatReservationReqDTO request) {

                SeatReservationResponse response = seatService.reserveSeats(
                                flightId,
                                request.getCabinClass(),
                                request.getSeatCount(),
                                request.getBookingReference());

                return ResponseEntity.ok(response);
        }
}