package com.project.notificationmanagement.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.project.notificationmanagement.dto.common.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler(NotificationNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotificationNotFoundException(
                        NotificationNotFoundException ex,
                        HttpServletRequest request) {

                LOGGER.error("Notification not found", ex);

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.NOT_FOUND.value())
                                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .errorCode("NOTIFICATION_001")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        @ExceptionHandler(EmailSendingException.class)
        public ResponseEntity<ErrorResponse> handleEmailSendingException(
                        EmailSendingException ex,
                        HttpServletRequest request) {

                LOGGER.error("Email sending failed", ex);

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                                .errorCode("EMAIL_001")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                Map<String, String> validationErrors = new HashMap<>();

                for (FieldError error : ex.getBindingResult().getFieldErrors()) {
                        validationErrors.put(error.getField(), error.getDefaultMessage());
                }

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .errorCode("VALIDATION_001")
                                .message("Validation failed.")
                                .validationErrors(validationErrors)
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(MissingServletRequestParameterException.class)
        public ResponseEntity<ErrorResponse> handleMissingParameterException(
                        MissingServletRequestParameterException ex,
                        HttpServletRequest request) {

                LOGGER.warn("Missing request parameter: {}", ex.getParameterName());

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .errorCode("REQUEST_001")
                                .message("Required request parameter '" + ex.getParameterName() + "' is missing.")
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
                        HttpMessageNotReadableException ex,
                        HttpServletRequest request) {

                LOGGER.warn("Malformed request body", ex);

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .errorCode("REQUEST_002")
                                .message("Invalid or malformed request body.")
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
                        MethodArgumentTypeMismatchException ex,
                        HttpServletRequest request) {

                LOGGER.warn("Invalid parameter type", ex);

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .errorCode("REQUEST_003")
                                .message("Invalid value for parameter '" + ex.getName() + "'.")
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {

                LOGGER.warn("Illegal argument", ex);

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .errorCode("REQUEST_004")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ErrorResponse> handleAuthenticationException(
                        AuthenticationException ex,
                        HttpServletRequest request) {

                LOGGER.warn("Authentication failed", ex);

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                                .errorCode("AUTH_001")
                                .message("Authentication failed.")
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDeniedException(
                        AccessDeniedException ex,
                        HttpServletRequest request) {

                LOGGER.warn("Access denied", ex);

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.FORBIDDEN.value())
                                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                                .errorCode("AUTH_002")
                                .message("You are not authorized to access this resource.")
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
                        DataIntegrityViolationException ex,
                        HttpServletRequest request) {

                LOGGER.error("Database constraint violation", ex);

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.CONFLICT.value())
                                .error(HttpStatus.CONFLICT.getReasonPhrase())
                                .errorCode("DATABASE_001")
                                .message("Database constraint violation.")
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleException(
                        Exception ex,
                        HttpServletRequest request) {

                LOGGER.error("Unhandled exception", ex);

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                                .errorCode("INTERNAL_001")
                                .message("An unexpected error occurred. Please try again later.")
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        @ExceptionHandler(RemoteServiceException.class)
        public ResponseEntity<ErrorResponse> handleRemoteServiceException(
                        RemoteServiceException ex,
                        HttpServletRequest request) {

                LOGGER.error("Remote service error", ex);

                ErrorResponse response = ErrorResponse.builder()
                                .success(false)
                                .status(HttpStatus.BAD_GATEWAY.value())
                                .error(HttpStatus.BAD_GATEWAY.getReasonPhrase())
                                .errorCode("REMOTE_001")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
        }
}