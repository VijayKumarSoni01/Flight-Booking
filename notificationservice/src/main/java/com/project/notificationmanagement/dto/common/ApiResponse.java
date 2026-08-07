package com.project.notificationmanagement.dto.common;

import java.time.LocalDateTime;

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
public class ApiResponse<T> {


    private boolean success;

    private int status;

    private String message;

    private T data;


    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();


    private String path;



    public static <T> ApiResponse<T> success(
            T data,
            String message) {


        return ApiResponse.<T>builder()

                .success(true)

                .status(200)

                .message(message)

                .data(data)

                .timestamp(LocalDateTime.now())

                .build();
    }





    public static <T> ApiResponse<T> success(
            int status,
            T data,
            String message) {


        return ApiResponse.<T>builder()

                .success(true)

                .status(status)

                .message(message)

                .data(data)

                .timestamp(LocalDateTime.now())

                .build();
    }





    public static <T> ApiResponse<T> failure(
            int status,
            String message) {


        return ApiResponse.<T>builder()

                .success(false)

                .status(status)

                .message(message)

                .data(null)

                .timestamp(LocalDateTime.now())

                .build();
    }

}