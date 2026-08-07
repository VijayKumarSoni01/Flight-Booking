package com.project.notificationmanagement.config.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.notificationmanagement.dto.common.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint 
        implements AuthenticationEntryPoint {


    private final ObjectMapper objectMapper;


    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {


        ErrorResponse errorResponse =
                ErrorResponse.builder()

                .success(false)

                .status(
                        HttpStatus.UNAUTHORIZED.value())

                .error(
                        HttpStatus.UNAUTHORIZED.getReasonPhrase())

                .errorCode(
                        "AUTH_001")

                .message(
                        "Authentication failed. Please provide a valid JWT token.")

                .path(
                        request.getRequestURI())

                .build();



        response.setStatus(
                HttpStatus.UNAUTHORIZED.value());


        response.setContentType(
                MediaType.APPLICATION_JSON_VALUE);



        objectMapper.writeValue(
                response.getOutputStream(),
                errorResponse);
    }

}