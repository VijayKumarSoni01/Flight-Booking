package com.project.bookingmanagement.service.interfaces;

import java.util.List;

import com.project.bookingmanagement.dto.passenger.request.AddPassengerRequest;
import com.project.bookingmanagement.dto.passenger.request.SeatSelectionRequest;
import com.project.bookingmanagement.dto.passenger.request.UpdatePassengerRequest;
import com.project.bookingmanagement.dto.passenger.response.PassengerDetailsResponse;
import com.project.bookingmanagement.dto.passenger.response.PassengerResponse;
import com.project.bookingmanagement.dto.passenger.response.PassengerSeatResponse;



public interface PassengerService {

    PassengerResponse addPassenger(
            Long bookingId,
            AddPassengerRequest request);

    PassengerResponse updatePassenger(
            Long passengerId,
            UpdatePassengerRequest request);

    PassengerDetailsResponse getPassengerById(Long passengerId);

    List<PassengerResponse> getPassengersByBooking(Long bookingId);

    PassengerSeatResponse selectSeat(
            Long passengerId,
            SeatSelectionRequest request);

    void removePassenger(Long passengerId);
}