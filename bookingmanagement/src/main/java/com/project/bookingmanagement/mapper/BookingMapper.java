package com.project.bookingmanagement.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.project.bookingmanagement.dto.booking.request.CreateBookingRequest;
import com.project.bookingmanagement.dto.booking.request.UpdateBookingRequest;
import com.project.bookingmanagement.dto.booking.response.BookingConfirmationResponse;
import com.project.bookingmanagement.dto.booking.response.BookingDetailsResponse;
import com.project.bookingmanagement.dto.booking.response.BookingResponse;
import com.project.bookingmanagement.dto.booking.response.BookingSummaryResponse;
import com.project.bookingmanagement.entity.Booking;

@Mapper(componentModel = "spring", uses = PassengerMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookingMapper {

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "bookingReference", ignore = true)
        @Mapping(target = "pnr", ignore = true)
        @Mapping(target = "userId", ignore = true)
        @Mapping(target = "paymentInfo", ignore = true)
        @Mapping(target = "totalPassengers", ignore = true)
        @Mapping(target = "totalAmount", ignore = true)
        @Mapping(target = "bookingStatus", ignore = true)
        @Mapping(target = "paymentStatus", ignore = true)
        @Mapping(target = "bookingDate", ignore = true)
        @Mapping(target = "travelDate", ignore = true)
        @Mapping(target = "expiresAt", ignore = true)
        @Mapping(target = "currency", ignore = true)
        @Mapping(target = "cancellationReason", ignore = true)
        @Mapping(target = "cancelledAt", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "version", ignore = true)
        Booking toEntity(CreateBookingRequest request);

        @Mapping(source = "id", target = "bookingId")
        @Mapping(source = "totalAmount", target = "totalFare")
        @Mapping(target = "flightNumber", ignore = true)
        @Mapping(target = "airlineName", ignore = true)
        @Mapping(target = "sourceAirport", ignore = true)
        @Mapping(target = "destinationAirport", ignore = true)
        BookingResponse toResponse(Booking booking);

        @Mapping(source = "id", target = "bookingId")
        @Mapping(source = "totalAmount", target = "totalFare")
        @Mapping(source = "updatedAt", target = "lastModifiedDate")
        @Mapping(target = "flightNumber", ignore = true)
        @Mapping(target = "airlineName", ignore = true)
        @Mapping(target = "sourceAirport", ignore = true)
        @Mapping(target = "destinationAirport", ignore = true)
        @Mapping(target = "departureTime", ignore = true)
        @Mapping(target = "arrivalTime", ignore = true)
        @Mapping(target = "baseFare", ignore = true)
        @Mapping(target = "taxAmount", ignore = true)
        @Mapping(target = "discountAmount", ignore = true)
        BookingDetailsResponse toDetailsResponse(Booking booking);

        @Mapping(source = "id", target = "bookingId")
        @Mapping(source = "totalAmount", target = "totalFare")
        @Mapping(target = "flightNumber", ignore = true)
        @Mapping(target = "airlineName", ignore = true)
        @Mapping(target = "sourceAirport", ignore = true)
        @Mapping(target = "destinationAirport", ignore = true)
        BookingSummaryResponse toSummaryResponse(Booking booking);

        List<BookingSummaryResponse> toSummaryResponseList(List<Booking> bookings);

        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "bookingReference", ignore = true)
        @Mapping(target = "pnr", ignore = true)
        @Mapping(target = "userId", ignore = true)
        @Mapping(target = "flightId", ignore = true)
        @Mapping(target = "cabinClass", ignore = true)
        @Mapping(target = "passengers", ignore = true)
        @Mapping(target = "paymentInfo", ignore = true)
        @Mapping(target = "totalPassengers", ignore = true)
        @Mapping(target = "totalAmount", ignore = true)
        @Mapping(target = "bookingStatus", ignore = true)
        @Mapping(target = "paymentStatus", ignore = true)
        @Mapping(target = "bookingDate", ignore = true)
        @Mapping(target = "travelDate", ignore = true)
        @Mapping(target = "expiresAt", ignore = true)
        @Mapping(target = "currency", ignore = true)
        @Mapping(target = "cancellationReason", ignore = true)
        @Mapping(target = "cancelledAt", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "version", ignore = true)
        void updateBooking(
                        UpdateBookingRequest request,
                        @MappingTarget Booking booking);

        @Mapping(source = "id", target = "bookingId")
        @Mapping(source = "totalAmount", target = "totalFare")
        @Mapping(target = "message", ignore = true)
        BookingConfirmationResponse toConfirmationResponse(Booking booking);
}