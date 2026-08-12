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
import com.project.usermanagment.service.UserService.Verification.EmailVerificationService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Slf4j
public class publicUserController {
        private final PublicUserService userService;
        private final EmailVerificationService emailVerificationService;

        @PostMapping("/login")
        @Operation(summary = "User Login", description = "Login using username/email/phone", tags = { "Public APIs" })
        public ResponseEntity<ApiResponse<UserAuthResponse>> login(
                        @Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {

                log.info("Login attempt for: {}", loginRequest.getIdentifier());

                String ipAddress = request.getHeader("X-Forwarded-For");

                if (ipAddress != null && !ipAddress.isBlank()) {
                        ipAddress = ipAddress.split(",")[0].trim();
                } else {
                        ipAddress = request.getRemoteAddr();
                }

                UserAuthResponse authResponse = userService.login(
                                loginRequest.getIdentifier(),
                                loginRequest.getPassword(),
                                ipAddress);

                return ResponseEntity.ok(
                                ApiResponse.success(authResponse, "User logged in successfully"));
        }

        @PostMapping("/register")
        @Operation(summary = "User Registration", description = "Register a new user", tags = { "Public APIs" })
        public ResponseEntity<ApiResponse<UserAuthResponse>> register(
                        @Valid @RequestBody RegistrationRequest request) {

                UserAuthResponse response = userService.register(request);

                return ResponseEntity.ok(ApiResponse.success(response, "User registered successfully"));
        }

        @PostMapping("/forgot-password")
        @Operation(summary = "Forgot Password", description = """
                        Send OTP for password reset.

                        EMAIL Example:
                        {
                          "identifier": "xxxxxx@gmail.com",
                          "otpType": "EMAIL"
                        }

                        PHONE Example:
                        {
                          "identifier": "+91xxxxxxxxxx",
                          "otpType": "PHONE"
                        }
                        """, tags = { "Public APIs" })
        public ResponseEntity<ApiResponse<String>> forgotPassword(
                        @Valid @RequestBody ForgotPassRequest request) {

                userService.initiatePasswordReset(
                                request.getIdentifier(),
                                request.getOtpType());

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                null,
                                                "OTP sent successfully"));
        }

        @PatchMapping("/reset-password")
        @Operation(summary = "Reset Password", description = """
                        Reset user password with OTP.

                        Example:
                        {
                          "email": "xxxxxx@gmail.com",
                          "otp": "123456",
                          "newPassword": "NewPassword123!"
                        }
                        """, tags = { "Public APIs" })
        public ResponseEntity<ApiResponse<String>> resetPassword(
                        @Valid @RequestBody ResetRequest request) {

                userService.resetPassword(
                                request.getEmail(),
                                request.getOtp(),
                                request.getNewPassword());

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                null,
                                                "Password reset successful"));
        }

        @PostMapping("/refresh-token")
        @Operation(summary = "Refresh Access Token", description = """
                        Refresh the access token using a valid refresh token.

                        Example:
                        {
                          "refreshToken": "your-refresh-token-here"
                        }
                        """, tags = { "Public APIs" })
        public ResponseEntity<ApiResponse<UserAuthResponse>> refreshToken(
                        @Valid @RequestBody RefreshTokenDTO request) {

                UserAuthResponse response = userService.refreshAccessToken(request.getRefreshToken());
                return ResponseEntity.ok(
                                ApiResponse.success(response, "Access token refreshed successfully"));
        }

        @PostMapping("/request-restore")
        @Operation(summary = "Request Account Restore", description = """
                        Used when a user has deleted/deactivated their account.

                        Example:
                        POST /api/public/request-restore?email=sonivk134@gmail.com

                        A restore link will be sent if the account is eligible for restoration.
                        """, tags = { "Public APIs" })
        public ResponseEntity<ApiResponse<String>> requestRestore(@RequestParam String email) {

                userService.generateRestoreToken(email);

                return ResponseEntity.ok(
                                ApiResponse.success(null, "If account exists, restore link sent"));
        }

        @PatchMapping("/restore")
        @Operation(summary = "Restore Account", description = """
                        Restore a previously deleted account.

                        Example:
                        POST /api/public/restore?token=abc123
                        """, tags = { "Public APIs" })
        public ResponseEntity<ApiResponse<String>> restore(@RequestParam String token) {

                userService.restoreWithToken(token);

                return ResponseEntity.ok(
                                ApiResponse.success(null, "Account reactivated successfully"));
        }

        // ---------------------Email Verification--------------------------

        @GetMapping(value = "/verify-email", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Verify Email", description = """
                        Verify user's email using the verification token
                        received in the registration email.

                        Example:
                        GET /api/public/verify-email?token=abc123
                        """, tags = { "Public APIs" })
        public ResponseEntity<ApiResponse<String>> verifyEmail(
                        @RequestParam String token) {

                emailVerificationService.verifyEmail(token);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                null,
                                                "Email verified successfully"));
        }

        @PostMapping("/resend-verification-email")
        @Operation(summary = "Resend Verification Email", description = """
                        Resend email verification link.

                        Request Body:
                        {
                          "email": "xxxxxxxx@gmail.com"
                        }
                        """, tags = { "Public APIs" })
        public ResponseEntity<ApiResponse<String>> resendVerificationEmail(
                        @RequestBody Map<String, String> body) {

                emailVerificationService
                                .resendVerificationEmail(
                                                body.get("email"));

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                null,
                                                "Verification email sent successfully"));
        }

}
