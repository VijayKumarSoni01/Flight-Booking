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

@Service
@RequiredArgsConstructor
@Transactional
public class SeatServiceImple implements SeatService {

        private final SeatRepository seatRepository;
        private final FlightRepository flightRepository;
        private final SeatMapper seatMapper;

        @Override
        @Transactional
        public SeatResDTO createSeat(SeatReqDTO request) {

                Flight flight = flightRepository.findById(request.getFlightId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Flight with ID " + request.getFlightId() + " not found."));

                String seatNumber = request.getSeatNumber()
                                .trim()
                                .toUpperCase();

                seatRepository.findByFlightIdAndSeatNumber(
                                request.getFlightId(),
                                seatNumber)
                                .ifPresent(seat -> {
                                        throw new IllegalArgumentException(
                                                        "Seat " + seatNumber + " already exists for this flight.");
                                });

                Seat seat = seatMapper.toEntity(request);

                seat.setFlight(flight);
                seat.setSeatNumber(seatNumber);

                String numericPart = seatNumber.replaceAll("[^0-9]", "");
                seat.setSeatIndex(Integer.parseInt(numericPart));

                if (seat.getSeatStatus() == null) {
                        seat.setSeatStatus(
                                        com.flightmanagement.flightmanagement.enums.SeatStatus.AVAILABLE);
                }

                Seat savedSeat = seatRepository.save(seat);

                return seatMapper.toDto(savedSeat);
        }

        @Override
        @Transactional(readOnly = true)
        public SeatResDTO getSeatById(Long id) {

                Seat seat = seatRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Seat with ID " + id + " not found."));

                return seatMapper.toDto(seat);
        }

        @Override
        @Transactional(readOnly = true)
        public List<SeatResDTO> getSeatsByFlight(Long flightId) {

                flightRepository.findById(flightId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Flight with ID " + flightId + " not found."));

                return seatRepository.findByFlightId(flightId)
                                .stream()
                                .map(seatMapper::toDto)
                                .toList();
        }

        @Override
        @Transactional
        public SeatResDTO updateSeat(Long id, SeatReqDTO request) {

                Seat seat = seatRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Seat with ID " + id + " not found."));

                Flight flight = flightRepository.findById(request.getFlightId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Flight with ID " + request.getFlightId() + " not found."));

                String seatNumber = request.getSeatNumber()
                                .trim()
                                .toUpperCase();

                seatRepository.findByFlightIdAndSeatNumber(
                                request.getFlightId(),
                                seatNumber)
                                .ifPresent(existingSeat -> {

                                        if (!existingSeat.getId().equals(id)) {
                                                throw new IllegalArgumentException(
                                                                "Seat " + seatNumber
                                                                                + " already exists for this flight.");
                                        }
                                });

                seatMapper.updateEntityFromDto(request, seat);

                seat.setFlight(flight);
                seat.setSeatNumber(seatNumber);

                Seat updatedSeat = seatRepository.save(seat);

                return seatMapper.toDto(updatedSeat);
        }

        @Override
        @Transactional
        public void deleteSeat(Long id) {

                Seat seat = seatRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Seat with ID " + id + " not found."));

                seatRepository.delete(seat);
        }

        @Override
        @Transactional(readOnly = true)
        public SeatAvailabilityResDTO getSeatAvailability(
                        Long flightId,
                        CabinClass cabinClass) {

                flightRepository.findById(flightId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Flight with ID " + flightId + " not found."));

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
        public List<String> holdSeats(SeatReservationReqDTO request) {

                Flight flight = flightRepository.findById(request.getFlightId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Flight with ID " + request.getFlightId() + " not found."));

                LocalDateTime holdTime = LocalDateTime.now();

                List<Seat> seatsToUpdate = new ArrayList<>();

                for (String seatNumber : request.getSeatNumbers()) {

                        String normalizedSeat = seatNumber.trim().toUpperCase();

                        Seat seat = seatRepository.findByFlightIdAndSeatNumber(
                                        flight.getId(),
                                        normalizedSeat)
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                        "Seat " + normalizedSeat + " not found."));

                        if (seat.getSeatStatus() != SeatStatus.AVAILABLE) {
                                throw new IllegalArgumentException(
                                                "Seat " + normalizedSeat + " is not available.");
                        }

                        seat.setSeatStatus(SeatStatus.HELD);
                        seat.setBookingReference(request.getBookingReference());
                        seat.setReservedAt(holdTime);

                        seatsToUpdate.add(seat);
                }

                seatRepository.saveAll(seatsToUpdate);

                return request.getSeatNumbers();
        }

        @Override
        @Transactional
        public SeatReservationResponse reserveSeats(
                        Long flightId,
                        CabinClass cabinClass,
                        Integer seatCount,
                        String bookingReference) {

                Flight flight = flightRepository.findById(flightId)
                                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

                List<Seat> availableSeats = seatRepository.findByFlightIdAndCabinClassAndSeatStatusOrderBySeatIndexAsc(
                                flight.getId(),
                                cabinClass,
                                SeatStatus.AVAILABLE);

                if (availableSeats.size() < seatCount) {
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
                                                        .build());
                }

                return SeatReservationResponse.builder()
                                .bookingReference(bookingReference)
                                .reservedCount(reservedSeats.size())
                                .seats(reservedSeats)
                                .build();
        }

        @Override
        @Transactional
        public void confirmSeats(String bookingReference) {

                List<Seat> seats = seatRepository.findByBookingReference(bookingReference);

                if (seats.isEmpty()) {
                        throw new IllegalArgumentException(
                                        "No seats found for booking reference: " + bookingReference);
                }

                for (Seat seat : seats) {

                        if (seat.getSeatStatus() != SeatStatus.HELD) {
                                throw new IllegalArgumentException(
                                                "Seat " + seat.getSeatNumber() + " is not currently held.");
                        }

                        seat.setSeatStatus(SeatStatus.BOOKED);
                        seat.setReservedAt(null);
                }

                seatRepository.saveAll(seats);
        }

        @Override
        @Transactional
        public void releaseSeats(String bookingReference) {

                System.out.println("Releasing seats for : " + bookingReference);

                List<Seat> seats = seatRepository.findByBookingReference(bookingReference);

                System.out.println("Seats found : " + seats.size());

                if (seats.isEmpty()) {
                        return;
                }

                for (Seat seat : seats) {

                        if (seat.getSeatStatus() == SeatStatus.HELD) {

                                seat.setSeatStatus(SeatStatus.AVAILABLE);
                                seat.setBookingReference(null);
                                seat.setReservedAt(null);

                                System.out.println("Released : " + seat.getSeatNumber());
                        }
                }

                seatRepository.saveAll(seats);

                System.out.println("Seats released successfully.");
        }

        @Override
        @Transactional
        public void generateSeats(Long flightId) {

                Flight flight = flightRepository.findById(flightId)
                                .orElseThrow(() -> new FlightNotFoundException(flightId));

                if (seatRepository.existsByFlightId(flightId)) {

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
