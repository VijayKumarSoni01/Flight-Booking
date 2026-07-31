package com.project.bookingmanagement.service.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bookingmanagement.client.FlightServiceClient;
import com.project.bookingmanagement.config.security.SecurityUtil;
import com.project.bookingmanagement.dto.booking.request.CancelBookingRequest;
import com.project.bookingmanagement.dto.booking.request.CreateBookingRequest;
import com.project.bookingmanagement.dto.booking.request.UpdateBookingRequest;
import com.project.bookingmanagement.dto.booking.response.BookingCancellationResponse;
import com.project.bookingmanagement.dto.booking.response.BookingConfirmationResponse;
import com.project.bookingmanagement.dto.booking.response.BookingDetailsResponse;
import com.project.bookingmanagement.dto.booking.response.BookingResponse;
import com.project.bookingmanagement.dto.booking.response.BookingSummaryResponse;
import com.project.bookingmanagement.dto.external.flight.FlightFareResponse;
import com.project.bookingmanagement.dto.external.flight.FlightResponse;
import com.project.bookingmanagement.dto.external.flight.SeatAvailabilityResponse;
import com.project.bookingmanagement.dto.external.flight.SeatReservationRequest;
import com.project.bookingmanagement.dto.passenger.request.AddPassengerRequest;
import com.project.bookingmanagement.entity.Booking;
import com.project.bookingmanagement.entity.BookingPassenger;
import com.project.bookingmanagement.enums.bookingEnum.BookingStatus;
import com.project.bookingmanagement.enums.bookingEnum.PaymentStatus;
import com.project.bookingmanagement.enums.bookingPassangerEnum.CabinClass;
import com.project.bookingmanagement.exception.BookingAlreadyCancelledException;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final PassengerMapper passengerMapper;
    private final FlightServiceClient flightServiceClient;
    private final BookingReferenceGenerator bookingReferenceGenerator;
    private final PnrGenerator pnrGenerator;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public BookingConfirmationResponse createBooking(CreateBookingRequest request) {

        try {

            // 1. Validate Flight
            validateFlight(request.getFlightId());

            FlightResponse flight = getFlight(request.getFlightId());

            // 2. Check Seat Availability
            checkSeatAvailability(
                    request.getFlightId(),
                    request.getCabinClass(),
                    request.getPassengers().size());

            // 3. Get Flight Fare
            FlightFareResponse fare = getFlightFare(
                    request.getFlightId(),
                    request.getCabinClass());

            if (fare == null) {
                throw new FlightNotAvailableException(
                        "Unable to fetch flight fare.");
            }

            // 4. Calculate Total Fare
            BigDecimal totalFare = calculateTotalFare(
                    request.getPassengers(),
                    fare);

            // 5. Create Booking Entity
            Booking booking = bookingMapper.toEntity(request);

            booking.setUserId(
                    securityUtil.getCurrentUserId());

            booking.setBookingReference(
                    bookingReferenceGenerator.generate());

            booking.setPnr(
                    pnrGenerator.generate());

            booking.setBookingStatus(BookingStatus.PENDING);
            booking.setPaymentStatus(PaymentStatus.PENDING);
            booking.setBookingDate(LocalDateTime.now());

            if (flight.getDepartureTime() == null) {
                throw new FlightNotAvailableException(
                        "Flight departure time is unavailable.");
            }

            booking.setTravelDate(
                    flight.getDepartureTime().toLocalDate());

            booking.setCurrency(fare.getCurrency());
            booking.setTotalPassengers(request.getPassengers().size());
            booking.setTotalAmount(totalFare);

            // 6. Map Passengers
            List<BookingPassenger> passengers = request.getPassengers()
                    .stream()
                    .map(passengerMapper::toEntity)
                    .peek(passenger -> passenger.setBooking(booking))
                    .toList();

            booking.setPassengers(passengers);

            // 7. Save Booking
            Booking savedBooking = bookingRepository.save(booking);

            // 8. Reserve Seats
            SeatReservationRequest seatRequest = new SeatReservationRequest();

            seatRequest.setCabinClass(request.getCabinClass());
            seatRequest.setSeatCount(request.getPassengers().size());
            seatRequest.setBookingReference(savedBooking.getBookingReference());

            flightServiceClient.reserveSeats(
                    request.getFlightId(),
                    seatRequest);

            // 9. Build Response
            BookingConfirmationResponse response = bookingMapper.toConfirmationResponse(savedBooking);

            response.setMessage(
                    "Booking created successfully. Seats reserved. Awaiting payment.");

            return response;

        } catch (FlightNotAvailableException
                | SeatAlreadyBookedException e) {

            throw e;

        } catch (feign.FeignException e) {

            throw new ExternalServiceException(
                    "Flight Management Service",
                    "Unable to communicate with Flight Management.",
                    e);
        }
    }

    private void validateFlight(Long flightId) {

        Boolean valid = flightServiceClient.validateFlight(flightId);

        if (Boolean.FALSE.equals(valid)) {
            throw new FlightNotAvailableException(flightId);
        }
    }

    private FlightResponse getFlight(Long flightId) {

        FlightResponse flight = flightServiceClient.getFlightById(flightId);

        if (flight == null) {
            throw new FlightNotAvailableException(flightId);
        }

        return flight;
    }

    private void checkSeatAvailability(
            Long flightId,
            CabinClass cabinClass,
            int passengerCount) {

        SeatAvailabilityResponse availability = flightServiceClient.checkSeatAvailability(
                flightId,
                cabinClass.name());

        if (availability == null
                || availability.getAvailableSeats() == null
                || availability.getAvailableSeats() < passengerCount) {

            throw new SeatAlreadyBookedException(
                    "Requested seats are not available.");
        }
    }

    private FlightFareResponse getFlightFare(
            Long flightId,
            CabinClass cabinClass) {

        FlightFareResponse fare = flightServiceClient.getFlightFare(
                flightId,
                cabinClass.name());

        if (fare == null) {
            throw new FlightNotAvailableException(
                    "Unable to fetch flight fare.");
        }

        return fare;
    }

    private BigDecimal calculateTotalFare(
            List<AddPassengerRequest> passengers,
            FlightFareResponse fare) {

        BigDecimal totalFare = BigDecimal.ZERO;

        for (AddPassengerRequest passenger : passengers) {

            switch (passenger.getPassengerType()) {

                case ADULT ->
                    totalFare = totalFare.add(fare.getAdultFare());

                case CHILD ->
                    totalFare = totalFare.add(fare.getChildFare());

                case INFANT ->
                    totalFare = totalFare.add(fare.getInfantFare());
            }
        }

        return totalFare;
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingByReference(String bookingReference) {

        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new BookingNotFoundException(
                        "Booking not found with reference: " + bookingReference));

        BookingResponse response = bookingMapper.toResponse(booking);

        FlightResponse flight = flightServiceClient.getFlightById(
                booking.getFlightId());

        populateBookingResponse(response, flight);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDetailsResponse getBookingById(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(
                        "Booking not found with id: " + bookingId));

        BookingDetailsResponse response = bookingMapper.toDetailsResponse(booking);

        FlightResponse flight = flightServiceClient.getFlightById(
                booking.getFlightId());

        populateDetailsFlightInformation(response, flight);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingSummaryResponse> getAllBookings() {

        List<Booking> bookings = bookingRepository.findAll();

        if (bookings.isEmpty()) {
            return List.of();
        }

        List<BookingSummaryResponse> responses = bookingMapper.toSummaryResponseList(bookings);

        for (int i = 0; i < bookings.size(); i++) {

            FlightResponse flight = flightServiceClient.getFlightById(
                    bookings.get(i).getFlightId());

            populateSummaryFlightDetails(
                    responses.get(i),
                    flight);
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingSummaryResponse> getBookingsByUser(Long userId) {

        List<Booking> bookings = bookingRepository.findByUserId(userId);

        List<BookingSummaryResponse> responses = bookingMapper.toSummaryResponseList(bookings);

        for (int i = 0; i < bookings.size(); i++) {

            FlightResponse flight = flightServiceClient.getFlightById(
                    bookings.get(i).getFlightId());

            populateSummaryFlightDetails(
                    responses.get(i),
                    flight);
        }

        return responses;
    }

    @Override
    @Transactional
    public BookingResponse updateBooking(
            Long bookingId,
            UpdateBookingRequest request) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(
                        "Booking not found with id: " + bookingId));

        bookingMapper.updateBooking(request, booking);

        Booking updatedBooking = bookingRepository.save(booking);

        BookingResponse response = bookingMapper.toResponse(updatedBooking);

        FlightResponse flight = flightServiceClient.getFlightById(
                updatedBooking.getFlightId());

        populateBookingResponse(response, flight);

        return response;
    }

    @Override
    @Transactional
    public BookingCancellationResponse cancelBooking(
            Long bookingId,
            CancelBookingRequest request) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(
                        "Booking not found with id: " + bookingId));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException(
                    "Booking is already cancelled.");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);

        booking.setCancellationReason(request.getCancellationReason());
        booking.setCancelledAt(LocalDateTime.now());

        Booking cancelledBooking = bookingRepository.save(booking);

        BookingCancellationResponse response = new BookingCancellationResponse();

        response.setBookingId(cancelledBooking.getId());
        response.setBookingReference(cancelledBooking.getBookingReference());
        response.setPnr(cancelledBooking.getPnr());
        response.setBookingStatus(cancelledBooking.getBookingStatus());
        response.setCancelledAt(cancelledBooking.getCancelledAt());
        response.setMessage("Booking cancelled successfully.");

        return response;
    }

    private void populateBookingResponse(
            BookingResponse response,
            FlightResponse flight) {

        if (flight == null) {
            return;
        }

        response.setFlightNumber(flight.getFlightNumber());
        response.setAirlineName(flight.getAirlineName());
        response.setSourceAirport(flight.getSourceAirport());
        response.setDestinationAirport(flight.getDestinationAirport());
    }

    private void populateSummaryFlightDetails(
            BookingSummaryResponse response,
            FlightResponse flight) {

        if (flight == null) {
            return;
        }

        response.setFlightNumber(flight.getFlightNumber());
        response.setAirlineName(flight.getAirlineName());
        response.setSourceAirport(flight.getSourceAirport());
        response.setDestinationAirport(flight.getDestinationAirport());
    }

    private void populateDetailsFlightInformation(
            BookingDetailsResponse response,
            FlightResponse flight) {

        if (flight == null) {
            return;
        }

        response.setFlightNumber(flight.getFlightNumber());
        response.setAirlineName(flight.getAirlineName());
        response.setSourceAirport(flight.getSourceAirport());
        response.setDestinationAirport(flight.getDestinationAirport());
        response.setDepartureTime(flight.getDepartureTime());
        response.setArrivalTime(flight.getArrivalTime());
    }

}