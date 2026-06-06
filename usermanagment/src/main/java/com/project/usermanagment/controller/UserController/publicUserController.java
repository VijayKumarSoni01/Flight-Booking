package com.project.usermanagment.controller.UserController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.usermanagment.dtos.UserDTO.OtherDTO.RefreshTokenDTO;
import com.project.usermanagment.dtos.UserDTO.passwordDTO.ForgotPassRequest;
import com.project.usermanagment.dtos.UserDTO.passwordDTO.ResetRequest;
import com.project.usermanagment.dtos.UserDTO.registrationORlogin.LoginRequest;
import com.project.usermanagment.dtos.UserDTO.registrationORlogin.RegistrationRequest;
import com.project.usermanagment.dtos.UserDTO.securitydto.ApiResponse;
import com.project.usermanagment.dtos.UserDTO.securitydto.UserAuthResponse;
import com.project.usermanagment.service.UserService.PublicUserService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class publicUserController {
        private final PublicUserService userService;

        @PostMapping("/login")
        @Operation(summary = "User Login", description = "Login using username/email/phone", tags = { "Public APIs" })
        public ResponseEntity<ApiResponse<UserAuthResponse>> login(
                        @Valid @RequestBody LoginRequest loginRequest) {

                log.info("Login attempt for: {}", loginRequest.getIdentifier());

                UserAuthResponse authResponse = userService.login(
                                loginRequest.getIdentifier(),
                                loginRequest.getPassword());

                return ResponseEntity.ok(
                                ApiResponse.success(authResponse, "User logged in successfully"));
        }

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<UserAuthResponse>> register(
                        @Valid @RequestBody RegistrationRequest request) {

                UserAuthResponse response = userService.register(request);

                return ResponseEntity.ok(ApiResponse.success(response, "User registered successfully"));
        }

        @PostMapping("/forgot-password")
        public ResponseEntity<ApiResponse<String>> forgotPassword(
                        @Valid @RequestBody ForgotPassRequest request) {

                log.info("Password reset requested for email: {}", request.getEmail());
                userService.initiatePasswordReset(request.getEmail());

                return ResponseEntity.ok(
                                ApiResponse.success(null, "If the email exists, a reset link has been sent"));
        }

        @PostMapping("/reset-password")
        public ResponseEntity<ApiResponse<String>> resetPassword(
                        @Valid @RequestBody ResetRequest request) {

                userService.resetPassword(
                                request.getToken(),
                                request.getNewPassword());

                return ResponseEntity.ok(
                                ApiResponse.success(null, "Password reset successful"));
        }

        @PostMapping("/refresh-token")
        public ResponseEntity<ApiResponse<UserAuthResponse>> refreshToken(
                        @Valid @RequestBody RefreshTokenDTO request) {

                UserAuthResponse response = userService.refreshAccessToken(request.getRefreshToken());
                return ResponseEntity.ok(
                                ApiResponse.success(response, "Access token refreshed successfully"));
        }

        @PostMapping("/request-restore")
        public ResponseEntity<ApiResponse<String>> requestRestore(@RequestParam String email) {

                userService.generateRestoreToken(email);

                return ResponseEntity.ok(
                                ApiResponse.success(null, "If account exists, restore link sent"));
        }

        @PostMapping("/restore")
        public ResponseEntity<ApiResponse<String>> restore(@RequestParam String token) {

                userService.restoreWithToken(token);

                return ResponseEntity.ok(
                                ApiResponse.success(null, "Account reactivated successfully"));
        }

}
