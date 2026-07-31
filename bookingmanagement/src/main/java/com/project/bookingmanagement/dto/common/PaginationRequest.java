package com.project.bookingmanagement.dto.common;

import com.project.bookingmanagement.enums.validation.SortDirection;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PaginationRequest {

    @Min(value = 0, message = "Page number cannot be negative")
    private int page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100")
    private int size = 10;

    private String sortBy = "createdAt";

    private SortDirection sortDirection = SortDirection.DESC;
}