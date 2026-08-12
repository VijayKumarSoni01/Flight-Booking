package com.project.bookingmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.bookingmanagement.dto.booking.internal.BookingValidationResponse;
import com.project.bookingmanagement.dto.booking.request.UpdateBookingPaymentStatusReqDTO;
import com.project.bookingmanagement.dto.booking.response.BookingConfirmationResponse;
import com.project.bookingmanagement.service.interfaces.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/internal/bookings")
@RequiredArgsConstructor
public class InternalBookingController {

    private final BookingService bookingService;

    @GetMapping("/reference/{bookingReference}")
    public ResponseEntity<BookingValidationResponse> getBookingByReference(
            @PathVariable String bookingReference) {

        return ResponseEntity.ok(
                bookingService.getBookingValidationByReference(bookingReference));
    }

    @PutMapping("/{bookingId}/payment-status")
    public ResponseEntity<Void> updatePaymentStatus(
            @PathVariable Long bookingId,
            @RequestBody UpdateBookingPaymentStatusReqDTO request) {

        bookingService.updatePaymentStatus(bookingId, request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<BookingConfirmationResponse> confirmBooking(
            @PathVariable Long bookingId) {

        BookingConfirmationResponse response = bookingService.confirmBooking(bookingId);

        return ResponseEntity.ok(response);
    }
}