package com.project.bookingmanagement.service.interfaces;

import java.util.List;

import com.project.bookingmanagement.dto.booking.request.CancelBookingRequest;
import com.project.bookingmanagement.dto.booking.request.CreateBookingRequest;
import com.project.bookingmanagement.dto.booking.request.UpdateBookingRequest;
import com.project.bookingmanagement.dto.booking.response.BookingCancellationResponse;
import com.project.bookingmanagement.dto.booking.response.BookingConfirmationResponse;
import com.project.bookingmanagement.dto.booking.response.BookingDetailsResponse;
import com.project.bookingmanagement.dto.booking.response.BookingResponse;
import com.project.bookingmanagement.dto.booking.response.BookingSummaryResponse;



public interface BookingService {

    BookingConfirmationResponse createBooking(CreateBookingRequest request);

    BookingDetailsResponse getBookingById(Long bookingId);

    BookingResponse getBookingByReference(String bookingReference);

    List<BookingSummaryResponse> getBookingsByUser(Long userId);

    List<BookingSummaryResponse> getAllBookings();

    BookingResponse updateBooking(Long bookingId,
                                  UpdateBookingRequest request);

    BookingCancellationResponse cancelBooking(
        Long bookingId,
        CancelBookingRequest request);

         BookingConfirmationResponse confirmBooking(Long bookingId);
}