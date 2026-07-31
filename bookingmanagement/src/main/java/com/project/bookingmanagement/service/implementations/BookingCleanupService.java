package com.project.bookingmanagement.service.implementations;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.project.bookingmanagement.enums.bookingEnum.BookingStatus;
import com.project.bookingmanagement.repository.BookingRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingCleanupService {

    private final BookingRepository bookingRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void deleteOldCancelledBookings() {

        LocalDateTime cutoff = LocalDateTime.now().minusYears(1);

        bookingRepository.deleteByBookingStatusAndCancelledAtBefore(
                BookingStatus.CANCELLED,
                cutoff);
    }
}