package com.project.bookingmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.project.bookingmanagement.dto.booking.request.CancelBookingRequest;
import com.project.bookingmanagement.dto.booking.request.UpdateBookingRequest;
import com.project.bookingmanagement.dto.booking.response.BookingCancellationResponse;
import com.project.bookingmanagement.dto.booking.response.BookingConfirmationResponse;
import com.project.bookingmanagement.dto.booking.response.BookingDetailsResponse;
import com.project.bookingmanagement.dto.booking.response.BookingResponse;
import com.project.bookingmanagement.dto.booking.response.BookingSummaryResponse;
import com.project.bookingmanagement.dto.common.ApiResponse;
import com.project.bookingmanagement.service.interfaces.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/private/admin/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingSummaryResponse>>> getAllBookings() {

        List<BookingSummaryResponse> response =
                bookingService.getAllBookings();

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "All bookings fetched successfully."));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDetailsResponse>> getBookingById(
            @PathVariable Long bookingId) {

        BookingDetailsResponse response =
                bookingService.getBookingById(bookingId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Booking fetched successfully."));
    }

    @GetMapping("/reference/{bookingReference}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingByReference(
            @PathVariable String bookingReference) {

        BookingResponse response =
                bookingService.getBookingByReference(bookingReference);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Booking fetched successfully."));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BookingSummaryResponse>>> getBookingsByUser(
            @PathVariable Long userId) {

        List<BookingSummaryResponse> response =
                bookingService.getBookingsByUser(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "User bookings fetched successfully."));
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingResponse>> updateBooking(
            @PathVariable Long bookingId,
            @Valid @RequestBody UpdateBookingRequest request) {

        BookingResponse response =
                bookingService.updateBooking(bookingId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Booking updated successfully."));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<BookingCancellationResponse>> cancelBooking(
            @PathVariable Long bookingId,
            @Valid @RequestBody CancelBookingRequest request) {

        BookingCancellationResponse response =
                bookingService.cancelBooking(bookingId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Booking cancelled successfully."));
    }

    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<ApiResponse<BookingConfirmationResponse>> confirmBooking(
            @PathVariable Long bookingId) {

        BookingConfirmationResponse response =
                bookingService.confirmBooking(bookingId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Booking confirmed successfully."));
    }

}