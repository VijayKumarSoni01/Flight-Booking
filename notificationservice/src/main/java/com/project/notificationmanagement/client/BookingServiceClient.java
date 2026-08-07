package com.project.notificationmanagement.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.notificationmanagement.config.feign.FeignConfig;
import com.project.notificationmanagement.dto.response.BookingResponse;



@FeignClient(
        name = "booking-service",
        url = "${services.booking.url}",
        configuration = FeignConfig.class
)
public interface BookingServiceClient {


    @GetMapping(
            "/api/internal/bookings/{bookingReference}"
    )
    BookingResponse getBookingByReference(
            @PathVariable("bookingReference") String bookingReference);

}