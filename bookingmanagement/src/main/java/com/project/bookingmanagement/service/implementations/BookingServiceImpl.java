package com.project.bookingmanagement.service.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bookingmanagement.client.FlightServiceClient;
import com.project.bookingmanagement.client.PaymentServiceClient;
import com.project.bookingmanagement.config.security.SecurityUtil;
import com.project.bookingmanagement.dto.booking.internal.BookingValidationResponse;
import com.project.bookingmanagement.dto.booking.request.CancelBookingRequest;
import com.project.bookingmanagement.dto.booking.request.CreateBookingRequest;
import com.project.bookingmanagement.dto.booking.request.RefundPaymentReqDTO;
import com.project.bookingmanagement.dto.booking.request.UpdateBookingPaymentStatusReqDTO;
import com.project.bookingmanagement.dto.booking.request.UpdateBookingRequest;
import com.project.bookingmanagement.dto.booking.response.BookingCancellationResponse;
import com.project.bookingmanagement.dto.booking.response.BookingConfirmationResponse;
import com.project.bookingmanagement.dto.booking.response.BookingDetailsResponse;
import com.project.bookingmanagement.dto.booking.response.BookingResponse;
import com.project.bookingmanagement.dto.booking.response.BookingSummaryResponse;
import com.project.bookingmanagement.dto.booking.response.RefundResponseDTO;
import com.project.bookingmanagement.dto.common.ApiResponse;
import com.project.bookingmanagement.dto.external.flight.FlightResponse;
import com.project.bookingmanagement.dto.external.flight.SeatAvailabilityResponse;
import com.project.bookingmanagement.dto.external.flight.SeatResDTO;
import com.project.bookingmanagement.dto.external.flight.SeatReservationRequest;
import com.project.bookingmanagement.dto.external.flight.SeatReservationResponse;
import com.project.bookingmanagement.entity.Booking;
import com.project.bookingmanagement.entity.BookingPassenger;
import com.project.bookingmanagement.enums.bookingEnum.BookingStatus;
import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;
import com.project.bookingmanagement.enums.bookingPassangerEnum.CabinClass;
import com.project.bookingmanagement.exception.BookingAlreadyCancelledException;
import com.project.bookingmanagement.exception.BookingCancellationException;
import com.project.bookingmanagement.exception.BookingNotFoundException;
import com.project.bookingmanagement.exception.ExternalServiceException;
import com.project.bookingmanagement.exception.FlightNotAvailableException;
import com.project.bookingmanagement.exception.SeatAlreadyBookedException;
import com.project.bookingmanagement.mapper.BookingMapper;
import com.project.bookingmanagement.mapper.PassengerMapper;
import com.project.bookingmanagement.repository.BookingRepository;
import com.project.bookingmanagement.service.interfaces.BookingService;
import com.project.bookingmanagement.util.BookingReferenceGenerator;
import com.project.bookingmanagement.util.PnrGenerator;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

        private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

        private final BookingRepository bookingRepository;
        private final BookingMapper bookingMapper;
        private final PassengerMapper passengerMapper;
        private final FlightServiceClient flightServiceClient;
        private final BookingReferenceGenerator bookingReferenceGenerator;
        private final PnrGenerator pnrGenerator;
        private final SecurityUtil securityUtil;
        private final PaymentServiceClient paymentServiceClient;

        // ============================================================
        // CREATE BOOKING
        // ============================================================

        @Override
        @Transactional
        public BookingConfirmationResponse createBooking(
                        CreateBookingRequest request) {

                log.info(
                                "Creating booking for UserId={}, FlightId={}",
                                securityUtil.getCurrentUserId(),
                                request.getFlightId());

                try {

                        validateFlight(request.getFlightId());

                        FlightResponse flight = getFlight(request.getFlightId());

                        checkSeatAvailability(
                                        request.getFlightId(),
                                        request.getCabinClass(),
                                        request.getPassengers().size());

                        BigDecimal totalFare = request.getTotalAmount();

                        if (totalFare == null) {
                                throw new IllegalArgumentException(
                                                "Total amount is required");
                        }

                        log.info(
                                        "Selected fare amount from request: {}",
                                        totalFare);

                        Booking booking = bookingMapper.toEntity(request);

                        booking.setUserId(
                                        securityUtil.getCurrentUserId());

                        booking.setBookingReference(
                                        bookingReferenceGenerator.generate());

                        booking.setPnr(
                                        pnrGenerator.generate());

                        booking.setBookingStatus(
                                        BookingStatus.PENDING);

                        booking.setPaymentStatus(
                                        PaymentStatus.PENDING);

                        LocalDateTime now = LocalDateTime.now();

                        booking.setBookingDate(now);

                        booking.setExpiresAt(
                                        now.plusMinutes(15));

                        booking.setTravelDate(
                                        flight.getDepartureTime().toLocalDate());

                        booking.setCurrency(
                                        flight.getCurrency());

                        booking.setTotalPassengers(
                                        request.getPassengers().size());

                        booking.setTotalAmount(
                                        totalFare);

                        List<BookingPassenger> passengers = request.getPassengers()
                                        .stream()
                                        .map(passengerMapper::toEntity)
                                        .peek(passenger -> passenger.setBooking(booking))
                                        .toList();

                        booking.setPassengers(passengers);

                        Booking savedBooking = bookingRepository.save(booking);

                        log.info(
                                        "Booking created successfully. Reference={}",
                                        savedBooking.getBookingReference());

                        log.info(
                                        "SELECTED SEATS FROM FRONTEND = {}",
                                        request.getSeatNumbers());

                        SeatReservationRequest seatRequest = new SeatReservationRequest();

                        seatRequest.setCabinClass(
                                        request.getCabinClass());

                        seatRequest.setSeatCount(
                                        request.getPassengers().size());

                        seatRequest.setBookingReference(
                                        savedBooking.getBookingReference());

                        seatRequest.setSeatNumbers(
                                        request.getSeatNumbers());

                        SeatReservationResponse seatResponse = flightServiceClient.reserveSeats(
                                        request.getFlightId(),
                                        seatRequest);

                        log.info(
                                        "Seats reserved successfully. Reference={}, Seats={}",
                                        savedBooking.getBookingReference(),
                                        seatResponse != null
                                                        ? seatResponse.getSeats()
                                                        : null);

                        BookingConfirmationResponse response = bookingMapper.toConfirmationResponse(
                                        savedBooking);

                        if (seatResponse != null
                                        && seatResponse.getSeats() != null) {

                                response.setSeatNumbers(
                                                seatResponse.getSeats()
                                                                .stream()
                                                                .filter(Objects::nonNull)
                                                                .map(seat -> seat.getSeatNumber())
                                                                .filter(Objects::nonNull)
                                                                .toList());
                        }

                        response.setMessage(
                                        "Booking created successfully. Seats reserved. Awaiting payment.");

                        return response;

                } catch (
                                FlightNotAvailableException
                                | SeatAlreadyBookedException ex) {

                        log.error(
                                        "Booking validation failed",
                                        ex);

                        throw ex;

                } catch (FeignException ex) {

                        log.error(
                                        "Flight service communication failed",
                                        ex);

                        throw new ExternalServiceException(
                                        "Flight Management Service",
                                        "Unable to communicate with Flight Management.",
                                        ex);

                } catch (Exception ex) {

                        log.error(
                                        "Unexpected error while creating booking",
                                        ex);

                        throw ex;
                }
        }

        // ============================================================
        // VALIDATE FLIGHT
        // ============================================================

        private void validateFlight(Long flightId) {

                log.info(
                                "Validating FlightId={}",
                                flightId);

                Boolean valid = flightServiceClient.validateFlight(
                                flightId);

                if (Boolean.FALSE.equals(valid)) {

                        log.warn(
                                        "Flight {} is not available.",
                                        flightId);

                        throw new FlightNotAvailableException(
                                        flightId);
                }

                log.info(
                                "Flight validation successful. FlightId={}",
                                flightId);
        }

        // ============================================================
        // GET FLIGHT
        // ============================================================

        private FlightResponse getFlight(Long flightId) {

                log.info(
                                "Fetching Flight Details. FlightId={}",
                                flightId);

                FlightResponse flight = flightServiceClient.getFlightById(
                                flightId);

                if (flight == null) {

                        log.warn(
                                        "Flight details not found. FlightId={}",
                                        flightId);

                        throw new FlightNotAvailableException(
                                        flightId);
                }

                log.info(
                                "Flight fetched successfully. FlightId={}, FlightNumber={}, Source={}, Destination={}",
                                flight.getFlightId(),
                                flight.getFlightNumber(),
                                flight.getSourceAirport(),
                                flight.getDestinationAirport());

                return flight;
        }

        // ============================================================
        // CHECK SEAT AVAILABILITY
        // ============================================================

        private void checkSeatAvailability(
                        Long flightId,
                        CabinClass cabinClass,
                        int passengerCount) {

                log.info(
                                "Checking seat availability. FlightId={}, CabinClass={}, Passengers={}",
                                flightId,
                                cabinClass,
                                passengerCount);

                SeatAvailabilityResponse availability = flightServiceClient.checkSeatAvailability(
                                flightId,
                                cabinClass.name());

                if (availability == null
                                || availability.getAvailableSeats() == null
                                || availability.getAvailableSeats() < passengerCount) {

                        log.warn(
                                        "Insufficient seats. Available={}, Requested={}",
                                        availability == null
                                                        ? 0
                                                        : availability.getAvailableSeats(),
                                        passengerCount);

                        throw new SeatAlreadyBookedException(
                                        "Requested seats are not available.");
                }

                log.info(
                                "Seat availability verified successfully.");
        }

        // ============================================================
        // GET BOOKING BY REFERENCE
        // ============================================================

        @Override
        @Transactional(readOnly = true)
        public BookingResponse getBookingByReference(
                        String bookingReference) {

                log.info(
                                "Fetching booking by reference: {}",
                                bookingReference);

                Booking booking = bookingRepository
                                .findByBookingReference(
                                                bookingReference)
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Booking not found. Reference={}",
                                                        bookingReference);

                                        return new BookingNotFoundException(
                                                        "Booking not found with reference: "
                                                                        + bookingReference);
                                });

                BookingResponse response = bookingMapper.toResponse(booking);

                FlightResponse flight = flightServiceClient.getFlightById(
                                booking.getFlightId());

                populateBookingResponse(
                                response,
                                flight);

                List<SeatResDTO> seats = flightServiceClient
                                .getSeatsByBookingReference(
                                                booking.getBookingReference());

                if (seats != null) {

                        response.setSeatNumbers(
                                        seats.stream()
                                                        .filter(Objects::nonNull)
                                                        .map(seat -> seat.getSeatNumber())
                                                        .filter(Objects::nonNull)
                                                        .toList());
                }

                log.info(
                                "Booking fetched successfully. Reference={}",
                                bookingReference);

                return response;
        }

        // ============================================================
        // GET BOOKING BY ID
        // ============================================================

        @Override
        @Transactional(readOnly = true)
        public BookingDetailsResponse getBookingById(
                        Long bookingId) {

                log.info(
                                "Fetching booking details. BookingId={}",
                                bookingId);

                Booking booking = bookingRepository
                                .findById(bookingId)
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Booking not found. BookingId={}",
                                                        bookingId);

                                        return new BookingNotFoundException(
                                                        "Booking not found with id: "
                                                                        + bookingId);
                                });

                BookingDetailsResponse response = bookingMapper.toDetailsResponse(
                                booking);

                FlightResponse flight = flightServiceClient.getFlightById(
                                booking.getFlightId());

                populateDetailsFlightInformation(
                                response,
                                flight);

                List<SeatResDTO> seats = flightServiceClient
                                .getSeatsByBookingReference(
                                                booking.getBookingReference());

                if (seats != null
                                && !seats.isEmpty()) {

                        response.setSeatNumbers(
                                        seats.stream()
                                                        .filter(Objects::nonNull)
                                                        .map(seat -> seat.getSeatNumber())
                                                        .filter(Objects::nonNull)
                                                        .toList());
                }

                log.info(
                                "Booking details fetched successfully. BookingId={}",
                                bookingId);

                return response;
        }

        // ============================================================
        // GET ALL BOOKINGS
        // ============================================================

        @Override
        @Transactional(readOnly = true)
        public List<BookingSummaryResponse> getAllBookings() {

                log.info(
                                "Fetching all bookings.");

                List<Booking> bookings = bookingRepository.findAll();

                if (bookings.isEmpty()) {

                        log.info(
                                        "No bookings found.");

                        return List.of();
                }

                List<BookingSummaryResponse> responses = bookingMapper.toSummaryResponseList(
                                bookings);

                for (int i = 0; i < bookings.size(); i++) {

                        FlightResponse flight = flightServiceClient.getFlightById(
                                        bookings.get(i).getFlightId());

                        populateSummaryFlightDetails(
                                        responses.get(i),
                                        flight);
                }

                log.info(
                                "Fetched {} bookings successfully.",
                                bookings.size());

                return responses;
        }

        // ============================================================
        // CONFIRM BOOKING
        // ============================================================

        @Override
        @Transactional
        public BookingConfirmationResponse confirmBooking(
                        Long bookingId) {

                Booking booking = bookingRepository.findById(
                                bookingId)
                                .orElseThrow(
                                                () -> new BookingNotFoundException(
                                                                "Booking not found with ID: "
                                                                                + bookingId));

                if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {

                        return BookingConfirmationResponse
                                        .builder()
                                        .bookingId(booking.getId())
                                        .bookingReference(
                                                        booking.getBookingReference())
                                        .pnr(booking.getPnr())
                                        .bookingStatus(
                                                        booking.getBookingStatus())
                                        .paymentStatus(
                                                        booking.getPaymentStatus())
                                        .bookingDate(
                                                        booking.getBookingDate())
                                        .totalFare(
                                                        booking.getTotalAmount())
                                        .message(
                                                        "Booking already confirmed.")
                                        .build();
                }

                if (booking.getBookingStatus() == BookingStatus.CANCELLED) {

                        throw new IllegalStateException(
                                        "Cancelled booking cannot be confirmed.");
                }

                if (booking.getPaymentStatus() != PaymentStatus.SUCCESS) {

                        throw new IllegalStateException(
                                        "Payment is not completed.");
                }

                if (booking.getPnr() == null
                                || booking.getPnr().isBlank()) {

                        booking.setPnr(
                                        pnrGenerator.generate());
                }

                booking.setBookingStatus(
                                BookingStatus.CONFIRMED);

                Booking savedBooking = bookingRepository.save(booking);

                flightServiceClient.confirmSeats(
                                savedBooking.getBookingReference());

                return BookingConfirmationResponse
                                .builder()
                                .bookingId(savedBooking.getId())
                                .bookingReference(
                                                savedBooking.getBookingReference())
                                .pnr(savedBooking.getPnr())
                                .bookingStatus(
                                                savedBooking.getBookingStatus())
                                .paymentStatus(
                                                savedBooking.getPaymentStatus())
                                .bookingDate(
                                                savedBooking.getBookingDate())
                                .totalFare(
                                                savedBooking.getTotalAmount())
                                .message(
                                                "Booking confirmed successfully.")
                                .build();
        }

        // ============================================================
        // GET BOOKINGS BY USER - PAGINATED
        // ============================================================

        @Override
        @Transactional(readOnly = true)
        public Page<BookingSummaryResponse> getBookingsByUser(
                        Long userId,
                        Pageable pageable) {

                log.info(
                                "Fetching bookings for UserId={}, Page={}, Size={}",
                                userId,
                                pageable.getPageNumber(),
                                pageable.getPageSize());

                Page<Booking> bookings = bookingRepository.findByUserId(
                                userId,
                                pageable);

                if (bookings.isEmpty()) {

                        log.info(
                                        "No bookings found for UserId={}",
                                        userId);

                        return Page.empty(pageable);
                }

                Page<BookingSummaryResponse> responses = bookings.map(booking -> {

                        BookingSummaryResponse response = bookingMapper.toSummaryResponse(
                                        booking);

                        FlightResponse flight = flightServiceClient
                                        .getFlightById(
                                                        booking.getFlightId());

                        /*
                         * IMPORTANT:
                         * Verify exactly what Flight Management
                         * returns here.
                         */
                        log.info(
                                        "FLIGHT RESPONSE FOR BOOKING {} -> flightId={}, flightNumber={}, airline={}, source={}, destination={}",
                                        booking.getId(),
                                        flight != null
                                                        ? flight.getFlightId()
                                                        : null,
                                        flight != null
                                                        ? flight.getFlightNumber()
                                                        : null,
                                        flight != null
                                                        ? flight.getAirlineName()
                                                        : null,
                                        flight != null
                                                        ? flight.getSourceAirport()
                                                        : null,
                                        flight != null
                                                        ? flight.getDestinationAirport()
                                                        : null);

                        populateSummaryFlightDetails(
                                        response,
                                        flight);

                        List<SeatResDTO> seats = flightServiceClient
                                        .getSeatsByBookingReference(
                                                        booking.getBookingReference());

                        if (seats != null) {

                                response.setSeatNumbers(
                                                seats.stream()
                                                                .filter(
                                                                                Objects::nonNull)
                                                                .map(
                                                                                seat -> seat.getSeatNumber())
                                                                .filter(
                                                                                Objects::nonNull)
                                                                .toList());
                        }

                        return response;
                });

                log.info(
                                "Fetched {} bookings for UserId={} on page {}",
                                responses.getNumberOfElements(),
                                userId,
                                pageable.getPageNumber());

                return responses;
        }

        // ============================================================
        // UPDATE BOOKING
        // ============================================================

        @Override
        @Transactional
        public BookingResponse updateBooking(
                        Long bookingId,
                        UpdateBookingRequest request) {

                log.info(
                                "Updating booking. BookingId={}",
                                bookingId);

                Booking booking = bookingRepository
                                .findById(bookingId)
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Booking not found. BookingId={}",
                                                        bookingId);

                                        return new BookingNotFoundException(
                                                        "Booking not found with id: "
                                                                        + bookingId);
                                });

                bookingMapper.updateBooking(
                                request,
                                booking);

                Booking updatedBooking = bookingRepository.save(
                                booking);

                BookingResponse response = bookingMapper.toResponse(
                                updatedBooking);

                FlightResponse flight = flightServiceClient.getFlightById(
                                updatedBooking.getFlightId());

                populateBookingResponse(
                                response,
                                flight);

                log.info(
                                "Booking updated successfully. BookingId={}",
                                bookingId);

                return response;
        }

        // ============================================================
        // CANCEL BOOKING
        // ============================================================

        @Override
        @Transactional
        public BookingCancellationResponse cancelBooking(
                        Long bookingId,
                        CancelBookingRequest request) {

                log.info(
                                "Cancelling booking. BookingId={}",
                                bookingId);

                Booking booking = bookingRepository.findById(
                                bookingId)
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Booking not found. BookingId={}",
                                                        bookingId);

                                        return new BookingNotFoundException(
                                                        "Booking not found with ID: "
                                                                        + bookingId);
                                });

                if (booking.getBookingStatus() == BookingStatus.CANCELLED) {

                        log.warn(
                                        "Booking already cancelled. BookingId={}",
                                        bookingId);

                        throw new BookingAlreadyCancelledException(
                                        "Booking is already cancelled.");
                }

                if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {

                        throw new BookingCancellationException(
                                        "Only confirmed bookings can be cancelled.");
                }

                if (booking.getPaymentStatus() != PaymentStatus.SUCCESS) {

                        throw new BookingCancellationException(
                                        "Booking payment is not successful.");
                }

                try {

                        // ----------------------------------------------------
                        // REFUND PAYMENT
                        // ----------------------------------------------------

                        RefundPaymentReqDTO refundRequest = new RefundPaymentReqDTO();

                        refundRequest.setBookingId(
                                        booking.getId());

                        refundRequest.setRefundAmount(
                                        booking.getTotalAmount());

                        refundRequest.setReason(
                                        request.getCancellationReason());

                        log.info(
                                        "Requesting refund for BookingId={}",
                                        booking.getId());

                        ApiResponse<RefundResponseDTO> apiResponse = paymentServiceClient.refundPayment(
                                        refundRequest);

                        RefundResponseDTO refundResponse = apiResponse.getData();

                        log.info(
                                        "Refund successful. GatewayRefundId={}",
                                        refundResponse.getGatewayRefundId());

                        // ----------------------------------------------------
                        // RELEASE SEATS
                        // ----------------------------------------------------

                        log.info(
                                        "Releasing seats for BookingReference={}",
                                        booking.getBookingReference());

                        flightServiceClient.releaseSeats(
                                        booking.getBookingReference());

                        log.info(
                                        "Seats released successfully. BookingReference={}",
                                        booking.getBookingReference());

                        // ----------------------------------------------------
                        // UPDATE BOOKING
                        // ----------------------------------------------------

                        booking.setBookingStatus(
                                        BookingStatus.CANCELLED);

                        booking.setPaymentStatus(
                                        PaymentStatus.REFUNDED);

                        booking.setCancellationReason(
                                        request.getCancellationReason());

                        booking.setCancelledAt(
                                        LocalDateTime.now());

                        log.info(
                                        "Step 3 - Before save");

                        Booking cancelledBooking = bookingRepository.saveAndFlush(
                                        booking);

                        log.info(
                                        "Saved booking: status={}, paymentStatus={}, reason={}, cancelledAt={}, version={}",
                                        cancelledBooking.getBookingStatus(),
                                        cancelledBooking.getPaymentStatus(),
                                        cancelledBooking.getCancellationReason(),
                                        cancelledBooking.getCancelledAt(),
                                        cancelledBooking.getVersion());

                        log.info(
                                        "Step 4 - After save");

                        log.info(
                                        "Booking cancelled successfully. BookingId={}",
                                        cancelledBooking.getId());

                        BookingCancellationResponse response = bookingMapper.toCancellationResponse(
                                        cancelledBooking);

                        response.setRefundAmount(
                                        refundResponse.getRefundAmount());

                        response.setGatewayRefundId(
                                        refundResponse.getGatewayRefundId());

                        response.setMessage(
                                        "Booking cancelled successfully.");

                        return response;

                } catch (Exception ex) {

                        log.error(
                                        "Booking cancellation failed. BookingId={}",
                                        bookingId,
                                        ex);

                        throw new BookingCancellationException(
                                        "Unable to cancel booking. Please try again later.");
                }
        }

        // ============================================================
        // POPULATE BOOKING RESPONSE
        // ============================================================

        private void populateBookingResponse(
                        BookingResponse response,
                        FlightResponse flight) {

                if (response == null
                                || flight == null) {

                        return;
                }

                response.setFlightNumber(
                                flight.getFlightNumber());

                response.setAirlineName(
                                flight.getAirlineName());

                response.setSourceAirport(
                                flight.getSourceAirport());

                response.setDestinationAirport(
                                flight.getDestinationAirport());
        }

        // ============================================================
        // POPULATE SUMMARY RESPONSE
        // ============================================================

        private void populateSummaryFlightDetails(
                        BookingSummaryResponse response,
                        FlightResponse flight) {

                if (response == null || flight == null) {
                        return;
                }

                response.setFlightId(
                                flight.getFlightId());

                response.setFlightNumber(
                                flight.getFlightNumber());

                response.setAirlineName(
                                flight.getAirlineName());

                response.setSourceAirport(
                                flight.getSourceAirport());

                response.setDestinationAirport(
                                flight.getDestinationAirport());
        }

        private void populateDetailsFlightInformation(
                        BookingDetailsResponse response,
                        FlightResponse flight) {

                if (response == null
                                || flight == null) {

                        return;
                }

                response.setFlightNumber(
                                flight.getFlightNumber());

                response.setAirlineName(
                                flight.getAirlineName());

                response.setSourceAirport(
                                flight.getSourceAirport());

                response.setDestinationAirport(
                                flight.getDestinationAirport());

                response.setDepartureTime(
                                flight.getDepartureTime());

                response.setArrivalTime(
                                flight.getArrivalTime());
        }

        @Override
        @Transactional
        public void updatePaymentStatus(
                        Long bookingId,
                        UpdateBookingPaymentStatusReqDTO request) {

                log.info(
                                "Updating payment status. BookingId={}, PaymentStatus={}",
                                bookingId,
                                request.getPaymentStatus());

                Booking booking = bookingRepository
                                .findById(bookingId)
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Booking not found. BookingId={}",
                                                        bookingId);

                                        return new BookingNotFoundException(
                                                        "Booking not found.");
                                });

                booking.setPaymentStatus(
                                request.getPaymentStatus());

                bookingRepository.save(booking);

                log.info(
                                "Payment status updated successfully. BookingId={}",
                                bookingId);
        }

        @Override
        @Transactional(readOnly = true)
        public BookingValidationResponse getBookingValidationByReference(
                        String bookingReference) {

                log.info(
                                "Validating booking. Reference={}",
                                bookingReference);

                Booking booking = bookingRepository
                                .findByBookingReference(
                                                bookingReference)
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Booking not found. Reference={}",
                                                        bookingReference);

                                        return new BookingNotFoundException(
                                                        "Booking not found.");
                                });

                BookingValidationResponse response = new BookingValidationResponse();

                response.setBookingId(
                                booking.getId());

                response.setBookingReference(
                                booking.getBookingReference());

                response.setUserId(
                                booking.getUserId());

                response.setTotalAmount(
                                booking.getTotalAmount());

                response.setCurrency(
                                booking.getCurrency().name());

                response.setBookingStatus(
                                booking.getBookingStatus());

                response.setPaymentStatus(
                                booking.getPaymentStatus());

                log.info(
                                "Booking validation completed. Reference={}",
                                bookingReference);

                return response;
        }
}