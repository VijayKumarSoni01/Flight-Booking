package com.project.bookingmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.project.bookingmanagement.config.jwt.CustomUserPrincipal;
import com.project.bookingmanagement.dto.booking.request.CancelBookingRequest;
import com.project.bookingmanagement.dto.booking.request.CreateBookingRequest;
import com.project.bookingmanagement.dto.booking.request.UpdateBookingRequest;
import com.project.bookingmanagement.dto.booking.response.BookingCancellationResponse;
import com.project.bookingmanagement.dto.booking.response.BookingConfirmationResponse;
import com.project.bookingmanagement.dto.booking.response.BookingDetailsResponse;
import com.project.bookingmanagement.dto.booking.response.BookingResponse;
import com.project.bookingmanagement.dto.booking.response.BookingSummaryResponse;
import com.project.bookingmanagement.dto.common.ApiResponse;
import com.project.bookingmanagement.service.interfaces.BookingService;
import com.project.bookingmanagement.util.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/private/user/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserBookingController {

    private final BookingService bookingService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CustomUserPrincipal>> getCurrentUser() {

        CustomUserPrincipal user = SecurityUtils.getCurrentUser();

        return ResponseEntity.ok(
                ApiResponse.success(
                        user,
                        "Current user fetched successfully."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingConfirmationResponse>> createBooking(
            @Valid @RequestBody CreateBookingRequest request) {

        BookingConfirmationResponse response =
                bookingService.createBooking(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        response,
                        "Booking created successfully."));
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

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingSummaryResponse>>> getMyBookings() {

        Long userId = SecurityUtils.getCurrentUserId();

        List<BookingSummaryResponse> response =
                bookingService.getBookingsByUser(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Bookings fetched successfully."));
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

}