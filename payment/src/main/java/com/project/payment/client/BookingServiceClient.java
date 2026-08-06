package com.project.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.payment.config.feign.FeignConfig;
import com.project.payment.dto.request.UpdateBookingPaymentStatusReqDTO;
import com.project.payment.dto.response.BookingValidationResDTO;

@FeignClient(
        name = "booking-management",
        url = "${booking.service.url}",
        configuration = FeignConfig.class)
public interface BookingServiceClient {

    @GetMapping("/api/internal/bookings/reference/{bookingReference}")
    BookingValidationResDTO getBookingByReference(
            @PathVariable("bookingReference") String bookingReference);

    @PutMapping("/api/internal/bookings/{bookingId}/payment-status")
    void updatePaymentStatus(
            @PathVariable("bookingId") Long bookingId,
            @RequestBody UpdateBookingPaymentStatusReqDTO request);

    @PostMapping("/api/internal/bookings/{bookingId}/confirm")
    void confirmBooking(
            @PathVariable("bookingId") Long bookingId);
}