package com.project.bookingmanagement.dto.booking.request;

import java.time.LocalDate;

import com.project.bookingmanagement.enums.bookingEnum.BookingStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BookingFilterRequest {

    private String bookingReference;

    private BookingStatus bookingStatus;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Long userId;

    private Long flightId;

    @Min(value = 0, message = "Page number cannot be negative")
    private Integer page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100")
    private Integer size = 10;

    @Pattern(
            regexp = "^(createdAt|bookingReference|bookingStatus|totalAmount|departureDate)$",
            message = "Invalid sort field")
    private String sortBy = "createdAt";

    @Pattern(
            regexp = "^(ASC|DESC)$",
            message = "Sort direction must be ASC or DESC")
    private String sortDirection = "DESC";
}
