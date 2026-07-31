package com.project.bookingmanagement.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.project.bookingmanagement.dto.passenger.request.AddPassengerRequest;
import com.project.bookingmanagement.dto.passenger.request.UpdatePassengerRequest;
import com.project.bookingmanagement.dto.passenger.response.PassengerDetailsResponse;
import com.project.bookingmanagement.dto.passenger.response.PassengerResponse;
import com.project.bookingmanagement.entity.BookingPassenger;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PassengerMapper {

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "booking", ignore = true)
        @Mapping(target = "seatNumber", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "version", ignore = true)
        BookingPassenger toEntity(AddPassengerRequest request);

        @Mapping(source = "id", target = "passengerId")
        @Mapping(target = "seatPreference", ignore = true)
        PassengerResponse toResponse(BookingPassenger passenger);

        @Mapping(source = "id", target = "passengerId")
        @Mapping(target = "seatPreference", ignore = true)
        PassengerDetailsResponse toDetailsResponse(BookingPassenger passenger);

        List<PassengerResponse> toResponseList(List<BookingPassenger> passengers);

        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "booking", ignore = true)
        @Mapping(target = "seatNumber", ignore = true)
        @Mapping(target = "title", ignore = true)
        @Mapping(target = "firstName", ignore = true)
        @Mapping(target = "middleName", ignore = true)
        @Mapping(target = "lastName", ignore = true)
        @Mapping(target = "dateOfBirth", ignore = true)
        @Mapping(target = "gender", ignore = true)
        @Mapping(target = "nationality", ignore = true)
        @Mapping(target = "passengerType", ignore = true)
        @Mapping(target = "passportNumber", ignore = true)
        @Mapping(target = "passportExpiry", ignore = true)
        @Mapping(target = "passportIssuingCountry", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "version", ignore = true)
        void updatePassenger(
                        UpdatePassengerRequest request,
                        @MappingTarget BookingPassenger passenger);
}