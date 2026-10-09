package com.project.bookingmanagement.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.project.bookingmanagement.config.feign.FeignConfig;
import com.project.bookingmanagement.dto.external.flight.FlightFareResponse;
import com.project.bookingmanagement.dto.external.flight.FlightResponse;
import com.project.bookingmanagement.dto.external.flight.SeatAvailabilityResponse;
import com.project.bookingmanagement.dto.external.flight.SeatResDTO;
import com.project.bookingmanagement.dto.external.flight.SeatReservationRequest;
import com.project.bookingmanagement.dto.external.flight.SeatReservationResponse;

@FeignClient(name = "flight-management", url = "${flight.service.url}", path = "/api/private/flights", contextId = "flightServiceClient", configuration = FeignConfig.class)
public interface FlightServiceClient {

        @GetMapping("/{flightId}")
        FlightResponse getFlightById(
                        @PathVariable("flightId") Long flightId);

        @GetMapping("/{flightId}/validate")
        Boolean validateFlight(
                        @PathVariable("flightId") Long flightId);

        @GetMapping("/{flightId}/fare/{cabinClass}")
        FlightFareResponse getFlightFare(
                        @PathVariable("flightId") Long flightId,
                        @PathVariable("cabinClass") String cabinClass);

        @GetMapping("/{flightId}/seat-availability/{cabinClass}")
        SeatAvailabilityResponse checkSeatAvailability(
                        @PathVariable("flightId") Long flightId,
                        @PathVariable("cabinClass") String cabinClass);

        @PostMapping("/{flightId}/reserve-seats")
        SeatReservationResponse reserveSeats(
                        @PathVariable("flightId") Long flightId,
                        @RequestBody SeatReservationRequest request);

        @PostMapping("/seats/confirm/{bookingReference}")
        void confirmSeats(
                        @PathVariable("bookingReference") String bookingReference);

        @GetMapping("/seats/{bookingReference}")
        List<SeatResDTO> getSeatsByBookingReference(
                        @PathVariable("bookingReference") String bookingReference);

        @PostMapping("/seats/release/{bookingReference}")
        void releaseSeats(
                        @PathVariable("bookingReference") String bookingReference);
}