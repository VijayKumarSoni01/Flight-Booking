package com.flightmanagement.flightmanagement.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.SeatReqDTO;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.SeatReservationReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatAvailabilityResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatReservationResponse;
import com.flightmanagement.flightmanagement.entity.Aircraft;
import com.flightmanagement.flightmanagement.entity.Flight;
import com.flightmanagement.flightmanagement.entity.Seat;
import com.flightmanagement.flightmanagement.enums.CabinClass;
import com.flightmanagement.flightmanagement.enums.SeatStatus;
import com.flightmanagement.flightmanagement.exception.FlightNotFoundException;
import com.flightmanagement.flightmanagement.exception.ResourceNotFoundException;
import com.flightmanagement.flightmanagement.exception.SeatAlreadyBookedException;
import com.flightmanagement.flightmanagement.mapper.SeatMapper;
import com.flightmanagement.flightmanagement.repository.FlightRepository;
import com.flightmanagement.flightmanagement.repository.SeatRepository;
import com.flightmanagement.flightmanagement.service.interFace.SeatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SeatServiceImple implements SeatService {

        private final SeatRepository seatRepository;
        private final FlightRepository flightRepository;
        private final SeatMapper seatMapper;

        @Override
        @Transactional
        public SeatResDTO createSeat(
                        SeatReqDTO request) {

                log.info(
                                "Creating seat. FlightId={}, SeatNumber={}",
                                request.getFlightId(),
                                request.getSeatNumber());

                Flight flight = flightRepository
                                .findById(request.getFlightId())
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Flight not found. FlightId={}",
                                                        request.getFlightId());

                                        return new FlightNotFoundException(
                                                        request.getFlightId());
                                });

                String seatNumber = request.getSeatNumber()
                                .trim()
                                .toUpperCase();

                seatRepository.findByFlightIdAndSeatNumber(
                                request.getFlightId(),
                                seatNumber)
                                .ifPresent(existingSeat -> {

                                        log.warn(
                                                        "Seat already exists. FlightId={}, SeatNumber={}",
                                                        request.getFlightId(),
                                                        seatNumber);

                                        throw new SeatAlreadyBookedException(
                                                        "Seat " + seatNumber
                                                                        + " already exists for this flight.");
                                });

                Seat seat = seatMapper.toEntity(request);

                seat.setFlight(flight);

                seat.setSeatNumber(seatNumber);

                String numericPart = seatNumber.replaceAll("[^0-9]", "");

                seat.setSeatIndex(
                                Integer.parseInt(numericPart));

                if (seat.getSeatStatus() == null) {

                        seat.setSeatStatus(
                                        SeatStatus.AVAILABLE);
                }

                Seat savedSeat = seatRepository.save(seat);

                log.info(
                                "Seat created successfully. SeatId={}, SeatNumber={}, FlightId={}",
                                savedSeat.getId(),
                                savedSeat.getSeatNumber(),
                                flight.getId());

                return seatMapper.toDto(savedSeat);
        }

        @Override
        @Transactional(readOnly = true)
        public SeatResDTO getSeatById(Long id) {

                log.info("Fetching seat. SeatId={}", id);

                Seat seat = seatRepository.findById(id)
                                .orElseThrow(() -> {

                                        log.warn("Seat not found. SeatId={}", id);

                                        return new ResourceNotFoundException(
                                                        "Seat not found with ID: " + id);
                                });

                log.info("Seat fetched successfully. SeatId={}", id);

                return seatMapper.toDto(seat);
        }

        @Override
        @Transactional(readOnly = true)
        public List<SeatResDTO> getSeatsByFlight(Long flightId) {

                log.info("Fetching seats for FlightId={}", flightId);

                flightRepository.findById(flightId)
                                .orElseThrow(() -> {

                                        log.warn("Flight not found. FlightId={}", flightId);

                                        return new FlightNotFoundException(flightId);
                                });

                List<SeatResDTO> seats = seatRepository.findByFlightId(flightId)
                                .stream()
                                .map(seatMapper::toDto)
                                .toList();

                log.info(
                                "Fetched {} seats for FlightId={}",
                                seats.size(),
                                flightId);

                return seats;
        }

        @Override
        @Transactional
        public SeatResDTO updateSeat(
                        Long id,
                        SeatReqDTO request) {

                log.info(
                                "Updating seat. SeatId={}, FlightId={}",
                                id,
                                request.getFlightId());

                Seat seat = seatRepository.findById(id)
                                .orElseThrow(() -> {

                                        log.warn("Seat not found. SeatId={}", id);

                                        return new ResourceNotFoundException(
                                                        "Seat not found with ID: " + id);
                                });

                Flight flight = flightRepository.findById(request.getFlightId())
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Flight not found. FlightId={}",
                                                        request.getFlightId());

                                        return new FlightNotFoundException(
                                                        request.getFlightId());
                                });

                String seatNumber = request.getSeatNumber()
                                .trim()
                                .toUpperCase();

                seatRepository.findByFlightIdAndSeatNumber(
                                request.getFlightId(),
                                seatNumber)
                                .ifPresent(existingSeat -> {

                                        if (!existingSeat.getId().equals(id)) {

                                                log.warn(
                                                                "Duplicate seat number. FlightId={}, SeatNumber={}",
                                                                request.getFlightId(),
                                                                seatNumber);

                                                throw new SeatAlreadyBookedException(
                                                                "Seat " + seatNumber
                                                                                + " already exists for this flight.");
                                        }
                                });

                seatMapper.updateEntityFromDto(
                                request,
                                seat);

                seat.setFlight(flight);
                seat.setSeatNumber(seatNumber);

                String numericPart = seatNumber.replaceAll("[^0-9]", "");

                seat.setSeatIndex(
                                Integer.parseInt(numericPart));

                Seat updatedSeat = seatRepository.save(seat);

                log.info(
                                "Seat updated successfully. SeatId={}, SeatNumber={}",
                                updatedSeat.getId(),
                                updatedSeat.getSeatNumber());

                return seatMapper.toDto(updatedSeat);
        }

        @Override
        @Transactional
        public void deleteSeat(Long id) {

                log.info("Deleting seat. SeatId={}", id);

                Seat seat = seatRepository.findById(id)
                                .orElseThrow(() -> {

                                        log.warn("Seat not found. SeatId={}", id);

                                        return new ResourceNotFoundException(
                                                        "Seat not found with ID: " + id);
                                });

                seatRepository.delete(seat);

                log.info("Seat deleted successfully. SeatId={}", id);
        }

        @Override
        @Transactional(readOnly = true)
        public SeatAvailabilityResDTO getSeatAvailability(
                        Long flightId,
                        CabinClass cabinClass) {

                log.info(
                                "Fetching seat availability. FlightId={}, CabinClass={}",
                                flightId,
                                cabinClass);

                flightRepository.findById(flightId)
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Flight not found. FlightId={}",
                                                        flightId);

                                        return new FlightNotFoundException(flightId);
                                });

                long availableSeats = seatRepository.countByFlightIdAndCabinClassAndSeatStatus(
                                flightId,
                                cabinClass,
                                SeatStatus.AVAILABLE);

                long bookedSeats = seatRepository.countByFlightIdAndCabinClassAndSeatStatus(
                                flightId,
                                cabinClass,
                                SeatStatus.BOOKED);

                long heldSeats = seatRepository.countByFlightIdAndCabinClassAndSeatStatus(
                                flightId,
                                cabinClass,
                                SeatStatus.HELD);

                long blockedSeats = seatRepository.countByFlightIdAndCabinClassAndSeatStatus(
                                flightId,
                                cabinClass,
                                SeatStatus.BLOCKED);

                long totalSeats = availableSeats +
                                bookedSeats +
                                heldSeats +
                                blockedSeats;

                log.info(
                                "Seat availability fetched successfully. FlightId={}, Available={}",
                                flightId,
                                availableSeats);

                return SeatAvailabilityResDTO.builder()
                                .flightId(flightId)
                                .cabinClass(cabinClass)
                                .totalSeats(totalSeats)
                                .availableSeats(availableSeats)
                                .bookedSeats(bookedSeats)
                                .heldSeats(heldSeats)
                                .blockedSeats(blockedSeats)
                                .build();
        }

        @Override
        @Transactional
        public List<String> holdSeats(
                        SeatReservationReqDTO request) {

                log.info(
                                "Holding seats. FlightId={}, BookingReference={}",
                                request.getFlightId(),
                                request.getBookingReference());

                Flight flight = flightRepository.findById(request.getFlightId())
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Flight not found. FlightId={}",
                                                        request.getFlightId());

                                        return new FlightNotFoundException(
                                                        request.getFlightId());
                                });

                LocalDateTime holdTime = LocalDateTime.now();

                List<Seat> seatsToUpdate = new ArrayList<>();

                for (String seatNumber : request.getSeatNumbers()) {

                        String normalizedSeat = seatNumber.trim().toUpperCase();

                        Seat seat = seatRepository.findByFlightIdAndSeatNumber(
                                        flight.getId(),
                                        normalizedSeat)
                                        .orElseThrow(() -> {

                                                log.warn(
                                                                "Seat not found. SeatNumber={}",
                                                                normalizedSeat);

                                                return new ResourceNotFoundException(
                                                                "Seat " + normalizedSeat + " not found.");
                                        });

                        if (seat.getSeatStatus() != SeatStatus.AVAILABLE) {

                                log.warn(
                                                "Seat already reserved. SeatNumber={}",
                                                normalizedSeat);

                                throw new SeatAlreadyBookedException(
                                                "Seat " + normalizedSeat + " is not available.");
                        }

                        seat.setSeatStatus(SeatStatus.HELD);
                        seat.setBookingReference(request.getBookingReference());
                        seat.setReservedAt(holdTime);

                        seatsToUpdate.add(seat);
                }

                seatRepository.saveAll(seatsToUpdate);

                log.info(
                                "{} seats held successfully for BookingReference={}",
                                seatsToUpdate.size(),
                                request.getBookingReference());

                return request.getSeatNumbers();
        }

        @Override
        @Transactional
        public SeatReservationResponse reserveSeats(
                        Long flightId,
                        CabinClass cabinClass,
                        Integer seatCount,
                        String bookingReference) {

                log.info(
                                "Reserving {} seats. FlightId={}, CabinClass={}, BookingReference={}",
                                seatCount,
                                flightId,
                                cabinClass,
                                bookingReference);

                Flight flight = flightRepository.findById(flightId)
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Flight not found. FlightId={}",
                                                        flightId);

                                        return new FlightNotFoundException(flightId);
                                });

                List<Seat> availableSeats = seatRepository.findByFlightIdAndCabinClassAndSeatStatusOrderBySeatIndexAsc(
                                flight.getId(),
                                cabinClass,
                                SeatStatus.AVAILABLE);

                if (availableSeats.size() < seatCount) {

                        log.warn(
                                        "Insufficient seats. Requested={}, Available={}",
                                        seatCount,
                                        availableSeats.size());

                        throw new SeatAlreadyBookedException(
                                        "Only " + availableSeats.size() + " seats available.");
                }

                LocalDateTime now = LocalDateTime.now();

                List<SeatResDTO> reservedSeats = new ArrayList<>();

                for (int i = 0; i < seatCount; i++) {

                        Seat seat = availableSeats.get(i);

                        seat.setSeatStatus(SeatStatus.HELD);
                        seat.setBookingReference(bookingReference);
                        seat.setReservedAt(now);

                        seatRepository.save(seat);

                        reservedSeats.add(
                                        SeatResDTO.builder()
                                                        .id(seat.getId())
                                                        .seatNumber(seat.getSeatNumber())
                                                        .cabinClass(seat.getCabinClass())
                                                        .seatStatus(seat.getSeatStatus())
                                                        .bookingReference(seat.getBookingReference())
                                                        .reservedAt(seat.getReservedAt())
                                                        .flightId(flight.getId())
                                                        .flightNumber(flight.getFlightNumber())
                                                        .build());
                }

                log.info(
                                "{} seats reserved successfully. BookingReference={}",
                                reservedSeats.size(),
                                bookingReference);

                return SeatReservationResponse.builder()
                                .bookingReference(bookingReference)
                                .reservedCount(reservedSeats.size())
                                .seats(reservedSeats)
                                .build();
        }

        @Override
        @Transactional
        public void confirmSeats(String bookingReference) {

                log.info(
                                "Confirming seats. BookingReference={}",
                                bookingReference);

                List<Seat> seats = seatRepository.findByBookingReference(
                                bookingReference);

                if (seats.isEmpty()) {

                        log.warn(
                                        "No seats found. BookingReference={}",
                                        bookingReference);

                        throw new ResourceNotFoundException(
                                        "No seats found for booking reference: "
                                                        + bookingReference);
                }

                for (Seat seat : seats) {

                        if (seat.getSeatStatus() != SeatStatus.HELD) {

                                log.warn(
                                                "Seat is not HELD. SeatNumber={}",
                                                seat.getSeatNumber());

                                throw new SeatAlreadyBookedException(
                                                "Seat " + seat.getSeatNumber()
                                                                + " is not currently held.");
                        }

                        seat.setSeatStatus(SeatStatus.BOOKED);
                        seat.setReservedAt(null);
                }

                seatRepository.saveAll(seats);

                log.info(
                                "{} seats confirmed successfully. BookingReference={}",
                                seats.size(),
                                bookingReference);
        }

        @Override
        @Transactional
        public void releaseSeats(String bookingReference) {

                log.info(
                                "Releasing seats. BookingReference={}",
                                bookingReference);

                List<Seat> seats = seatRepository.findByBookingReference(
                                bookingReference);

                if (seats.isEmpty()) {

                        log.warn(
                                        "No seats found for release. BookingReference={}",
                                        bookingReference);

                        return;
                }

                int releasedCount = 0;

                for (Seat seat : seats) {

                        if (seat.getSeatStatus() == SeatStatus.HELD
                                        || seat.getSeatStatus() == SeatStatus.BOOKED) {

                                seat.setSeatStatus(SeatStatus.AVAILABLE);
                                seat.setBookingReference(null);
                                seat.setReservedAt(null);

                                releasedCount++;
                        }
                }

                seatRepository.saveAll(seats);

                log.info(
                                "{} seats released successfully. BookingReference={}",
                                releasedCount,
                                bookingReference);
        }

        @Override
        @Transactional
        public void generateSeats(Long flightId) {

                log.info(
                                "Generating seats. FlightId={}",
                                flightId);

                Flight flight = flightRepository.findById(flightId)
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Flight not found. FlightId={}",
                                                        flightId);

                                        return new FlightNotFoundException(flightId);
                                });

                if (seatRepository.existsByFlightId(flightId)) {

                        log.warn(
                                        "Seats already generated. FlightId={}",
                                        flightId);

                        throw new IllegalStateException(
                                        "Seats have already been generated for Flight ID: "
                                                        + flightId);
                }

                Aircraft aircraft = flight.getAircraft();

                List<Seat> seats = new ArrayList<>();

                generateCabinSeats(
                                seats,
                                flight,
                                CabinClass.ECONOMY,
                                aircraft.getEconomySeats(),
                                "E");

                generateCabinSeats(
                                seats,
                                flight,
                                CabinClass.PREMIUM_ECONOMY,
                                aircraft.getPremiumEconomySeats(),
                                "PE");

                generateCabinSeats(
                                seats,
                                flight,
                                CabinClass.BUSINESS,
                                aircraft.getBusinessSeats(),
                                "B");

                generateCabinSeats(
                                seats,
                                flight,
                                CabinClass.FIRST,
                                aircraft.getFirstClassSeats(),
                                "F");

                seatRepository.saveAll(seats);

                log.info(
                                "{} seats generated successfully. FlightId={}",
                                seats.size(),
                                flightId);
        }

        private void generateCabinSeats(
                        List<Seat> seats,
                        Flight flight,
                        CabinClass cabinClass,
                        int seatCount,
                        String prefix) {

                if (seatCount <= 0) {
                        return;
                }

                for (int i = 1; i <= seatCount; i++) {

                        Seat seat = Seat.builder()
                                        .flight(flight)
                                        .cabinClass(cabinClass)
                                        .seatNumber(prefix + String.format("%03d", i))
                                        .seatIndex(i)
                                        .seatStatus(SeatStatus.AVAILABLE)
                                        .bookingReference(null)
                                        .reservedAt(null)
                                        .build();

                        seats.add(seat);
                }
        }

}
