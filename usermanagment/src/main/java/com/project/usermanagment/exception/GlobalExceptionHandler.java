package com.project.usermanagment.exception;

import com.project.usermanagment.dtos.UserDTO.securitydto.ApiResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
                        MethodArgumentNotValidException ex) {

                Map<String, String> errors = new HashMap<>();

                ex.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> errors.put(
                                                error.getField(),
                                                error.getDefaultMessage()));

                log.error("VALIDATION ERROR", ex);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error(
                                                "Validation failed: " + errors));
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiResponse<String>> handleIllegalArgument(
                        IllegalArgumentException ex) {

                log.error("ILLEGAL ARGUMENT ERROR", ex);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error(
                                                ex.getMessage()));
        }

        @ExceptionHandler(UnauthorizedAccessException.class)
        public ResponseEntity<ApiResponse<String>> handleUnauthorized(
                        UnauthorizedAccessException ex) {

                log.error("UNAUTHORIZED ERROR", ex);

                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(ApiResponse.error(
                                                ex.getMessage()));
        }

        @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
        public ResponseEntity<ApiResponse<String>> handleOptimisticLocking(
                        ObjectOptimisticLockingFailureException ex) {

                log.error("OPTIMISTIC LOCKING ERROR", ex);

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(ApiResponse.error(
                                                "Data already updated by another request. Please refresh."));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Object>> handleGeneral(
                        Exception ex) {

                log.error("========== UNHANDLED EXCEPTION ==========", ex);

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.error(
                                                ex.getMessage()));
        }
}