package com.project.bookingmanagement.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.project.bookingmanagement.client.FlightServiceClient;
import com.project.bookingmanagement.entity.Booking;
import com.project.bookingmanagement.enums.bookingEnum.BookingStatus;
import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;
import com.project.bookingmanagement.repository.BookingRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingExpiryScheduler {

    private final BookingRepository bookingRepository;
    private final FlightServiceClient flightServiceClient;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void expireBookings() {

        List<Booking> expiredBookings =
                bookingRepository.findByBookingStatusAndExpiresAtBefore(
                        BookingStatus.PENDING,
                        LocalDateTime.now());

        log.info("Expired bookings found: {}", expiredBookings.size());

        for (Booking booking : expiredBookings) {

            log.info("Processing booking: {}", booking.getBookingReference());

            booking.setBookingStatus(BookingStatus.EXPIRED);
            booking.setPaymentStatus(PaymentStatus.EXPIRED);

            try {

                log.info("Calling Flight Service...");

                flightServiceClient.releaseSeats(
                        booking.getBookingReference());

                log.info("Seats released successfully for {}",
                        booking.getBookingReference());

            } catch (Exception e) {

                log.error("Failed to release seats for {}",
                        booking.getBookingReference(),
                        e);
            }
        }

        bookingRepository.saveAll(expiredBookings);
    }
}