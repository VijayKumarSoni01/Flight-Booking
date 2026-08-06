package com.project.bookingmanagement.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bookingmanagement.client.FlightServiceClient;
import com.project.bookingmanagement.entity.Booking;
import com.project.bookingmanagement.enums.bookingEnum.BookingStatus;
import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;
import com.project.bookingmanagement.repository.BookingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingExpiryScheduler {

    private final BookingRepository bookingRepository;
    private final FlightServiceClient flightServiceClient;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void expireBookings() {

        log.info("Checking for expired pending bookings...");

        List<Booking> expiredBookings =
                bookingRepository.findByBookingStatusAndExpiresAtBefore(
                        BookingStatus.PENDING,
                        LocalDateTime.now());

        if (expiredBookings.isEmpty()) {

            log.info("No expired pending bookings found.");
            return;
        }

        log.info("Found {} expired booking(s).", expiredBookings.size());

        for (Booking booking : expiredBookings) {

            try {

                log.info(
                        "Expiring booking. BookingId={}, BookingReference={}",
                        booking.getId(),
                        booking.getBookingReference());

                // Release held seats
                flightServiceClient.releaseSeats(
                        booking.getBookingReference());

                log.info(
                        "Seats released successfully. BookingReference={}",
                        booking.getBookingReference());

                // Mark booking as expired
                booking.setBookingStatus(BookingStatus.EXPIRED);
                booking.setPaymentStatus(PaymentStatus.EXPIRED);

                bookingRepository.save(booking);

                log.info(
                        "Booking expired successfully. BookingId={}",
                        booking.getId());

            } catch (Exception ex) {

                log.error(
                        "Failed to expire booking. BookingReference={}",
                        booking.getBookingReference(),
                        ex);
            }
        }

        log.info("Booking expiry scheduler completed.");
    }
}