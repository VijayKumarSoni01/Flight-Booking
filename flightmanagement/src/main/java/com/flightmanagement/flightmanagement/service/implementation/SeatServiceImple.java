package com.flightmanagement.flightmanagement.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.SeatReqDTO;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.SeatReservationReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatAvailabilityResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatReservationResponse;
import com.flightmanagement.flightmanagement.entity.Aircraft;
import com.flightmanagement.flightmanagement.entity.Flight;
import com.flightmanagement.flightmanagement.entity.FlightInstance;
import com.flightmanagement.flightmanagement.entity.FlightInstanceSeat;
import com.flightmanagement.flightmanagement.entity.Seat;
import com.flightmanagement.flightmanagement.enums.CabinClass;
import com.flightmanagement.flightmanagement.enums.SeatStatus;
import com.flightmanagement.flightmanagement.exception.FlightNotFoundException;
import com.flightmanagement.flightmanagement.exception.ResourceNotFoundException;
import com.flightmanagement.flightmanagement.exception.SeatAlreadyBookedException;
import com.flightmanagement.flightmanagement.mapper.SeatMapper;
import com.flightmanagement.flightmanagement.repository.FlightInstanceRepository;
import com.flightmanagement.flightmanagement.repository.FlightInstanceSeatRepository;
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

        private final FlightInstanceRepository flightInstanceRepository;

        private final FlightInstanceSeatRepository flightInstanceSeatRepository;

        private final SeatMapper seatMapper;

        @Override
        @Transactional
        public SeatResDTO createSeat(SeatReqDTO request) {

                log.info(
                                "Creating physical seat. FlightId={}, SeatNumber={}",
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

                seatRepository
                                .findByFlightIdAndSeatNumber(
                                                request.getFlightId(),
                                                seatNumber)
                                .ifPresent(existingSeat -> {

                                        throw new SeatAlreadyBookedException(
                                                        "Seat " + seatNumber
                                                                        + " already exists for this flight.");
                                });

                Seat seat = seatMapper.toEntity(request);

                seat.setFlight(flight);

                seat.setSeatNumber(seatNumber);

                String numericPart = seatNumber.replaceAll("[^0-9]", "");

                if (numericPart.isEmpty()) {
                        throw new IllegalArgumentException(
                                        "Seat number must contain a numeric part.");
                }

                seat.setSeatIndex(
                                Integer.parseInt(numericPart));

                Seat savedSeat = seatRepository.save(seat);

                return seatMapper.toDto(savedSeat);
        }

        @Override
        @Transactional(readOnly = true)
        public SeatResDTO getSeatById(Long id) {

                Seat seat = seatRepository
                                .findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Seat not found with ID: " + id));

                return seatMapper.toDto(seat);
        }

        @Override
        @Transactional(readOnly = true)
        public List<SeatResDTO> getSeatsByFlight(Long flightId) {

                Long flightInstanceId = flightId;

                log.info(
                                "Fetching seats for FlightInstanceId={}",
                                flightInstanceId);

                FlightInstance flightInstance = flightInstanceRepository
                                .findById(flightInstanceId)
                                .orElseThrow(() -> {

                                        log.warn(
                                                        "Flight instance not found. FlightInstanceId={}",
                                                        flightInstanceId);

                                        return new ResourceNotFoundException(
                                                        "Flight Instance with ID "
                                                                        + flightInstanceId
                                                                        + " not found.");
                                });

                Long masterFlightId = flightInstance
                                .getSchedule()
                                .getFlight()
                                .getId();

                List<Seat> physicalSeats = seatRepository.findByFlightId(
                                masterFlightId);

                List<FlightInstanceSeat> instanceSeats = flightInstanceSeatRepository
                                .findByFlightInstanceId(
                                                flightInstanceId);

                Map<Long, FlightInstanceSeat> instanceSeatMap = instanceSeats.stream()
                                .collect(
                                                Collectors.toMap(
                                                                fis -> fis.getSeat().getId(),
                                                                fis -> fis));

                List<SeatResDTO> result = new ArrayList<>();

                for (Seat seat : physicalSeats) {

                        SeatResDTO dto = seatMapper.toDto(seat);

                        dto.setFlightId(
                                        flightInstanceId);

                        FlightInstanceSeat instanceSeat = instanceSeatMap.get(
                                        seat.getId());

                        if (instanceSeat != null) {

                                /*
                                 * Date-specific status.
                                 */
                                dto.setSeatStatus(
                                                instanceSeat.getSeatStatus());

                                dto.setBookingReference(
                                                instanceSeat.getBookingReference());

                                dto.setReservedAt(
                                                instanceSeat.getReservedAt());

                        } else {

                                /*
                                 * If the instance seat has not been
                                 * generated yet, show it as available.
                                 *
                                 * We should later make sure instance seats
                                 * are generated when FlightInstance is created.
                                 */
                                dto.setSeatStatus(
                                                SeatStatus.AVAILABLE);

                                dto.setBookingReference(null);

                                dto.setReservedAt(null);
                        }

                        result.add(dto);
                }

                log.info(
                                "Fetched {} seats for FlightInstanceId={}",
                                result.size(),
                                flightInstanceId);

                return result;
        }

        @Override
        @Transactional
        public SeatResDTO updateSeat(
                        Long id,
                        SeatReqDTO request) {

                Seat seat = seatRepository
                                .findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Seat not found with ID: " + id));

                Flight flight = flightRepository
                                .findById(request.getFlightId())
                                .orElseThrow(() -> new FlightNotFoundException(
                                                request.getFlightId()));

                String seatNumber = request.getSeatNumber()
                                .trim()
                                .toUpperCase();

                seatRepository
                                .findByFlightIdAndSeatNumber(
                                                request.getFlightId(),
                                                seatNumber)
                                .ifPresent(existingSeat -> {

                                        if (!existingSeat.getId().equals(id)) {

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

                if (numericPart.isEmpty()) {

                        throw new IllegalArgumentException(
                                        "Seat number must contain a numeric part.");
                }

                seat.setSeatIndex(
                                Integer.parseInt(numericPart));

                Seat updatedSeat = seatRepository.save(seat);

                return seatMapper.toDto(updatedSeat);
        }

        @Override
        @Transactional
        public void deleteSeat(Long id) {

                Seat seat = seatRepository
                                .findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Seat not found with ID: " + id));

                seatRepository.delete(seat);
        }

        @Override
        @Transactional(readOnly = true)
        public SeatAvailabilityResDTO getSeatAvailability(
                        Long flightId,
                        CabinClass cabinClass) {

                Long flightInstanceId = flightId;

                log.info(
                                "Fetching seat availability. FlightInstanceId={}, CabinClass={}",
                                flightInstanceId,
                                cabinClass);

                flightInstanceRepository
                                .findById(flightInstanceId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Flight Instance with ID "
                                                                + flightInstanceId
                                                                + " not found."));

                long availableSeats = flightInstanceSeatRepository
                                .countByFlightInstanceIdAndSeat_CabinClassAndSeatStatus(
                                                flightInstanceId,
                                                cabinClass,
                                                SeatStatus.AVAILABLE);

                long bookedSeats = flightInstanceSeatRepository
                                .countByFlightInstanceIdAndSeat_CabinClassAndSeatStatus(
                                                flightInstanceId,
                                                cabinClass,
                                                SeatStatus.BOOKED);

                long heldSeats = flightInstanceSeatRepository
                                .countByFlightInstanceIdAndSeat_CabinClassAndSeatStatus(
                                                flightInstanceId,
                                                cabinClass,
                                                SeatStatus.HELD);

                long blockedSeats = flightInstanceSeatRepository
                                .countByFlightInstanceIdAndSeat_CabinClassAndSeatStatus(
                                                flightInstanceId,
                                                cabinClass,
                                                SeatStatus.BLOCKED);

                long totalSeats = availableSeats
                                + bookedSeats
                                + heldSeats
                                + blockedSeats;

                return SeatAvailabilityResDTO
                                .builder()
                                .flightId(flightInstanceId)
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

                Long flightInstanceId = request.getFlightId();

                log.info(
                                "Holding seats. FlightInstanceId={}, BookingReference={}",
                                flightInstanceId,
                                request.getBookingReference());

                /*
                 * Verify FlightInstance.
                 */
                flightInstanceRepository
                                .findById(flightInstanceId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Flight Instance with ID "
                                                                + flightInstanceId
                                                                + " not found."));

                LocalDateTime holdTime = LocalDateTime.now();

                List<FlightInstanceSeat> seatsToUpdate = new ArrayList<>();

                for (String seatNumber : request.getSeatNumbers()) {

                        String normalizedSeat = seatNumber
                                        .trim()
                                        .toUpperCase();

                        FlightInstanceSeat instanceSeat = flightInstanceSeatRepository
                                        .findByFlightInstanceIdAndSeat_SeatNumber(
                                                        flightInstanceId,
                                                        normalizedSeat)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Seat "
                                                                        + normalizedSeat
                                                                        + " not found for this flight instance."));

                        if (instanceSeat.getSeatStatus() != SeatStatus.AVAILABLE) {

                                throw new SeatAlreadyBookedException(
                                                "Seat "
                                                                + normalizedSeat
                                                                + " is not available.");
                        }

                        instanceSeat.setSeatStatus(
                                        SeatStatus.HELD);

                        instanceSeat.setBookingReference(
                                        request.getBookingReference());

                        instanceSeat.setReservedAt(
                                        holdTime);

                        seatsToUpdate.add(
                                        instanceSeat);
                }

                flightInstanceSeatRepository
                                .saveAll(seatsToUpdate);

                log.info(
                                "{} seats held successfully. BookingReference={}",
                                seatsToUpdate.size(),
                                request.getBookingReference());

                return request.getSeatNumbers();
        }

        @Override
        @Transactional
        public SeatReservationResponse reserveSeats(
                        SeatReservationReqDTO request) {

                Long flightInstanceId = request.getFlightId();

                String bookingReference = request.getBookingReference();

                List<String> requestedSeatNumbers = request.getSeatNumbers();

                CabinClass cabinClass = request.getCabinClass();

                Integer seatCount = request.getSeatCount();

                log.info(
                                "Reserving seats. FlightInstanceId={}, CabinClass={}, SeatCount={}, SelectedSeats={}, BookingReference={}",
                                flightInstanceId,
                                cabinClass,
                                seatCount,
                                requestedSeatNumbers,
                                bookingReference);

                FlightInstance flightInstance = flightInstanceRepository
                                .findById(flightInstanceId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Flight Instance with ID "
                                                                + flightInstanceId
                                                                + " not found."));

                if (seatCount == null || seatCount <= 0) {

                        throw new IllegalArgumentException(
                                        "Seat count must be greater than zero.");
                }

                if (cabinClass == null) {

                        throw new IllegalArgumentException(
                                        "Cabin class is required.");
                }

                List<FlightInstanceSeat> seatsToReserve = new ArrayList<>();

                if (requestedSeatNumbers != null
                                && !requestedSeatNumbers.isEmpty()) {

                        if (requestedSeatNumbers.size() != seatCount) {

                                throw new IllegalArgumentException(
                                                "Number of selected seats must match passenger count.");
                        }

                        for (String seatNumber : requestedSeatNumbers) {

                                if (seatNumber == null || seatNumber.isBlank()) {

                                        throw new IllegalArgumentException(
                                                        "Seat number cannot be empty.");
                                }

                                String normalizedSeatNumber = seatNumber
                                                .trim()
                                                .toUpperCase();

                                FlightInstanceSeat instanceSeat = flightInstanceSeatRepository
                                                .findByFlightInstanceIdAndSeatNumberForUpdate(
                                                                flightInstanceId,
                                                                normalizedSeatNumber)
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                "Seat "
                                                                                + normalizedSeatNumber
                                                                                + " not found for this flight."));

                                log.info(
                                                "Locked seat row. Seat={}, InstanceSeatId={}, Status={}, Version={}",
                                                normalizedSeatNumber,
                                                instanceSeat.getId(),
                                                instanceSeat.getSeatStatus(),
                                                instanceSeat.getVersion());

                                if (instanceSeat.getSeat().getCabinClass() != cabinClass) {

                                        throw new SeatAlreadyBookedException(
                                                        "Seat "
                                                                        + normalizedSeatNumber
                                                                        + " does not belong to "
                                                                        + cabinClass
                                                                        + " cabin.");
                                }

                                if (instanceSeat.getSeatStatus() != SeatStatus.AVAILABLE) {

                                        throw new SeatAlreadyBookedException(
                                                        "Seat "
                                                                        + normalizedSeatNumber
                                                                        + " is not available. Current status: "
                                                                        + instanceSeat.getSeatStatus());
                                }

                                if (seatsToReserve.contains(instanceSeat)) {

                                        throw new IllegalArgumentException(
                                                        "Duplicate seat selected: "
                                                                        + normalizedSeatNumber);
                                }

                                seatsToReserve.add(instanceSeat);
                        }
                }

                else {

                        log.info(
                                        "No seats selected. Automatically assigning seats.");

                        List<FlightInstanceSeat> availableSeats = flightInstanceSeatRepository
                                        .findAvailableSeatsForUpdate(
                                                        flightInstanceId,
                                                        cabinClass,
                                                        SeatStatus.AVAILABLE,
                                                        PageRequest.of(0, seatCount));

                        if (availableSeats.size() < seatCount) {

                                throw new SeatAlreadyBookedException(
                                                "Only "
                                                                + availableSeats.size()
                                                                + " seats available.");
                        }

                        seatsToReserve.addAll(availableSeats);
                }

                LocalDateTime now = LocalDateTime.now();

                List<SeatResDTO> reservedSeats = new ArrayList<>();

                for (FlightInstanceSeat instanceSeat : seatsToReserve) {

                        instanceSeat.setSeatStatus(
                                        SeatStatus.HELD);

                        instanceSeat.setBookingReference(
                                        bookingReference);

                        instanceSeat.setReservedAt(
                                        now);

                        Seat physicalSeat = instanceSeat.getSeat();

                        reservedSeats.add(
                                        SeatResDTO.builder()
                                                        .id(physicalSeat.getId())
                                                        .seatNumber(
                                                                        physicalSeat.getSeatNumber())
                                                        .cabinClass(
                                                                        physicalSeat.getCabinClass())
                                                        .seatStatus(
                                                                        instanceSeat.getSeatStatus())
                                                        .bookingReference(
                                                                        instanceSeat.getBookingReference())
                                                        .reservedAt(
                                                                        instanceSeat.getReservedAt())
                                                        .flightId(
                                                                        flightInstanceId)
                                                        .flightNumber(
                                                                        flightInstance
                                                                                        .getSchedule()
                                                                                        .getFlight()
                                                                                        .getFlightNumber())
                                                        .build());
                }

                flightInstanceSeatRepository.saveAll(
                                seatsToReserve);

                log.info(
                                "{} seats held successfully. BookingReference={}, Seats={}",
                                reservedSeats.size(),
                                bookingReference,
                                reservedSeats.stream()
                                                .filter(seat -> seat != null)
                                                .map(seat -> seat.getSeatNumber())
                                                .filter(seatNumber -> seatNumber != null)
                                                .toList());
                return SeatReservationResponse
                                .builder()
                                .bookingReference(bookingReference)
                                .reservedCount(reservedSeats.size())
                                .seats(reservedSeats)
                                .build();
        }

        @Override
        @Transactional
        public void confirmSeats(
                        String bookingReference) {

                log.info(
                                "Confirming seats. BookingReference={}",
                                bookingReference);

                List<FlightInstanceSeat> seats = flightInstanceSeatRepository
                                .findByBookingReference(
                                                bookingReference);

                if (seats.isEmpty()) {

                        throw new ResourceNotFoundException(
                                        "No seats found for booking reference: "
                                                        + bookingReference);
                }

                for (FlightInstanceSeat instanceSeat : seats) {

                        if (instanceSeat.getSeatStatus() != SeatStatus.HELD) {

                                throw new SeatAlreadyBookedException(
                                                "Seat "
                                                                + instanceSeat
                                                                                .getSeat()
                                                                                .getSeatNumber()
                                                                + " is not currently held.");
                        }

                        instanceSeat.setSeatStatus(
                                        SeatStatus.BOOKED);

                        instanceSeat.setReservedAt(null);
                }

                flightInstanceSeatRepository
                                .saveAll(seats);

                log.info(
                                "{} seats confirmed successfully. BookingReference={}",
                                seats.size(),
                                bookingReference);
        }

        @Override
        @Transactional
        public void releaseSeats(
                        String bookingReference) {

                log.info(
                                "Releasing seats. BookingReference={}",
                                bookingReference);

                List<FlightInstanceSeat> seats = flightInstanceSeatRepository
                                .findByBookingReference(
                                                bookingReference);

                if (seats.isEmpty()) {

                        log.warn(
                                        "No seats found for release. BookingReference={}",
                                        bookingReference);

                        return;
                }

                int releasedCount = 0;

                for (FlightInstanceSeat instanceSeat : seats) {

                        if (instanceSeat.getSeatStatus() == SeatStatus.HELD
                                        ||
                                        instanceSeat.getSeatStatus() == SeatStatus.BOOKED) {

                                instanceSeat.setSeatStatus(
                                                SeatStatus.AVAILABLE);

                                instanceSeat.setBookingReference(
                                                null);

                                instanceSeat.setReservedAt(
                                                null);

                                releasedCount++;
                        }
                }

                flightInstanceSeatRepository
                                .saveAll(seats);

                log.info(
                                "{} seats released successfully. BookingReference={}",
                                releasedCount,
                                bookingReference);
        }

        @Override
        @Transactional
        public void generateSeats(Long flightId) {

                log.info(
                                "Generating physical seats. FlightId={}",
                                flightId);

                Flight flight = flightRepository
                                .findById(flightId)
                                .orElseThrow(() -> new FlightNotFoundException(
                                                flightId));

                if (seatRepository
                                .existsByFlightId(flightId)) {

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
                                "{} physical seats generated. FlightId={}",
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
                                        .seatNumber(
                                                        prefix + String.format("%03d", i))
                                        .seatIndex(i)
                                        .build();

                        seats.add(seat);
                }
        }

        @Override
        @Transactional(readOnly = true)
        public List<SeatResDTO> getSeatsByBookingReference(
                        String bookingReference) {

                log.info(
                                "Fetching seats by BookingReference={}",
                                bookingReference);

                List<FlightInstanceSeat> instanceSeats = flightInstanceSeatRepository
                                .findByBookingReference(bookingReference);

                if (instanceSeats.isEmpty()) {

                        log.warn(
                                        "No seats found for BookingReference={}",
                                        bookingReference);

                        return List.of();
                }

                return instanceSeats.stream()
                                .map(instanceSeat -> {

                                        Seat physicalSeat = instanceSeat.getSeat();

                                        FlightInstance flightInstance = instanceSeat.getFlightInstance();

                                        return SeatResDTO.builder()
                                                        .id(physicalSeat.getId())
                                                        .seatNumber(
                                                                        physicalSeat.getSeatNumber())
                                                        .cabinClass(
                                                                        physicalSeat.getCabinClass())
                                                        .seatStatus(
                                                                        instanceSeat.getSeatStatus())
                                                        .bookingReference(
                                                                        instanceSeat.getBookingReference())
                                                        .reservedAt(
                                                                        instanceSeat.getReservedAt())
                                                        .flightId(
                                                                        flightInstance.getId())
                                                        .flightNumber(
                                                                        flightInstance
                                                                                        .getSchedule()
                                                                                        .getFlight()
                                                                                        .getFlightNumber())
                                                        .build();
                                })
                                .toList();
        }
}