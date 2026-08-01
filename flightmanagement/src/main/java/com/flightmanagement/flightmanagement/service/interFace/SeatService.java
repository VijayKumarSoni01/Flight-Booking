package com.flightmanagement.flightmanagement.service.interFace;

import java.util.List;

import com.flightmanagement.flightmanagement.dtos.requestDTOs.SeatReqDTO;
import com.flightmanagement.flightmanagement.dtos.requestDTOs.SeatReservationReqDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatAvailabilityResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatResDTO;
import com.flightmanagement.flightmanagement.dtos.responseDTOs.SeatReservationResponse;
import com.flightmanagement.flightmanagement.enums.CabinClass;

public interface SeatService {

    SeatResDTO createSeat(SeatReqDTO request);

    SeatResDTO getSeatById(Long id);

    List<SeatResDTO> getSeatsByFlight(Long flightId);

    SeatResDTO updateSeat(Long id, SeatReqDTO request);

    void deleteSeat(Long id);

    SeatAvailabilityResDTO getSeatAvailability(
            Long flightId,
            CabinClass cabinClass);

    List<String> holdSeats(SeatReservationReqDTO request);

    void confirmSeats(String bookingReference);

    void releaseSeats(String bookingReference);

    SeatReservationResponse reserveSeats(
        Long flightId,
        CabinClass cabinClass,
        Integer seatCount,
        String bookingReference);

        void generateSeats(Long flightId);

}