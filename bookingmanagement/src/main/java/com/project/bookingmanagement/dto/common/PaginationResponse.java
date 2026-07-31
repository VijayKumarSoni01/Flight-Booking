package com.project.bookingmanagement.dto.common;

import lombok.Data;

@Data
public class PaginationResponse {

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;

    private boolean first;

    private boolean last;

    private int numberOfElements;

    private boolean empty;
}