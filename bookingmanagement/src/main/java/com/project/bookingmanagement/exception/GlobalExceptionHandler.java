package com.project.bookingmanagement.exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.project.bookingmanagement.dto.common.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler(BookingNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleBookingNotFound(
                        BookingNotFoundException ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                ex.getMessage(),
                                Collections.emptyList(),
                                request.getRequestURI());
        }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<ErrorResponse> handleNoResourceFound(
                        NoResourceFoundException ex,
                        HttpServletRequest request) {

                ErrorResponse error = ErrorResponse.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .message("Endpoint not found.")
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(PassengerNotFoundException.class)
        public ResponseEntity<ErrorResponse> handlePassengerNotFound(
                        PassengerNotFoundException ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                ex.getMessage(),
                                Collections.emptyList(),
                                request.getRequestURI());
        }

        @ExceptionHandler(FlightNotAvailableException.class)
        public ResponseEntity<ErrorResponse> handleFlightNotAvailable(
                        FlightNotAvailableException ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                ex.getMessage(),
                                Collections.emptyList(),
                                request.getRequestURI());
        }

        @ExceptionHandler(BookingAlreadyCancelledException.class)
        public ResponseEntity<ErrorResponse> handleBookingAlreadyCancelled(
                        BookingAlreadyCancelledException ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage(),
                                Collections.emptyList(),
                                request.getRequestURI());
        }

        @ExceptionHandler(SeatAlreadyBookedException.class)
        public ResponseEntity<ErrorResponse> handleSeatAlreadyBooked(
                        SeatAlreadyBookedException ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.CONFLICT,
                                ex.getMessage(),
                                Collections.emptyList(),
                                request.getRequestURI());
        }

        @ExceptionHandler(CouponNotValidException.class)
        public ResponseEntity<ErrorResponse> handleCouponNotValid(
                        CouponNotValidException ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage(),
                                Collections.emptyList(),
                                request.getRequestURI());
        }

        @ExceptionHandler(BookingValidationException.class)
        public ResponseEntity<ErrorResponse> handleBookingValidation(
                        BookingValidationException ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage(),
                                ex.getValidationErrors(),
                                request.getRequestURI());
        }

        @ExceptionHandler(ExternalServiceException.class)
        public ResponseEntity<ErrorResponse> handleExternalService(
                        ExternalServiceException ex,
                        HttpServletRequest request) {

                log.error("External service exception", ex);

                return buildErrorResponse(
                                HttpStatus.SERVICE_UNAVAILABLE,
                                ex.getMessage(),
                                Collections.emptyList(),
                                request.getRequestURI());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                List<String> validationErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(fieldError -> fieldError.getDefaultMessage() != null
                                                ? fieldError.getDefaultMessage()
                                                : "Validation error")
                                .toList();

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Validation failed.",
                                validationErrors,
                                request.getRequestURI());
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraintViolation(
                        ConstraintViolationException ex,
                        HttpServletRequest request) {

                List<String> validationErrors = ex.getConstraintViolations()
                                .stream()
                                .map(violation -> violation.getMessage())
                                .toList();

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Validation failed.",
                                validationErrors,
                                request.getRequestURI());
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage(),
                                Collections.emptyList(),
                                request.getRequestURI());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(
                        Exception ex,
                        HttpServletRequest request) {

                log.error("Unexpected exception", ex);

                return buildErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred.",
                                Collections.emptyList(),
                                request.getRequestURI());
        }

        private ResponseEntity<ErrorResponse> buildErrorResponse(
                        HttpStatus status,
                        String message,
                        List<String> validationErrors,
                        String path) {

                ErrorResponse response = new ErrorResponse();

                response.setTimestamp(LocalDateTime.now());
                response.setStatus(status.value());
                response.setError(status.getReasonPhrase());
                response.setMessage(message);
                response.setPath(path);
                response.setValidationErrors(validationErrors);

                return ResponseEntity.status(status).body(response);
        }
}