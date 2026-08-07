package com.project.notificationmanagement.dto.common;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {

    private boolean success;

    private int status;

    private String message;

    private List<T> content;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;

    private int numberOfElements;

    private boolean first;

    private boolean last;

    private boolean empty;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private String path;
}