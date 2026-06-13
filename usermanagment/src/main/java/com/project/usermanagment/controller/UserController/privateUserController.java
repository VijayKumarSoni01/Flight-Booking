package com.project.usermanagment.controller.UserController;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.usermanagment.dtos.UserDTO.OtherDTO.UpdateDTO;
import com.project.usermanagment.dtos.UserDTO.OtherDTO.VerifyOtpDTO;
import com.project.usermanagment.dtos.UserDTO.passwordDTO.ChangePassRequest;
import com.project.usermanagment.dtos.UserDTO.securitydto.ApiResponse;
import com.project.usermanagment.entity.User;
import com.project.usermanagment.service.UserService.NumberVerificationService;
import com.project.usermanagment.service.UserService.PrivateUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/private")
@RequiredArgsConstructor
@Slf4j
public class privateUserController {

    private final PrivateUserService userService;
    private final NumberVerificationService numberVerificationService;
    // private final UserDetailsService userDetailsService;

    @PutMapping("/update-profile")
    public ResponseEntity<ApiResponse<User>> updateProfile(@Valid @RequestBody UpdateDTO update,
            Authentication authentication) {
        String email = authentication.getName();

        log.info("Updating profile for user: {}", email);

        User updatedUser = userService.updateProfile(email, update);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "Profile updated successfully"));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePassRequest req,
            Authentication auth) {
        String email = auth.getName();

        log.info("Password change request for user: {}", email);
        log.info("Password change request details: {}", req);
        userService.changePassword(email, req);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteUser(Authentication auth) {

        String email = auth.getName();

        log.info("User account deactivated for email: {}", email);
        userService.deactivateCurrentUser(email);

        return ResponseEntity.ok(
                ApiResponse.success(null, "User Deactivated Successfully"));
    }

    @PostMapping("/send-phone-otp")
    public ResponseEntity<ApiResponse<String>> sendPhoneOtp(
            Authentication authentication) {

        String email = authentication.getName();

        numberVerificationService.sendOtp(email);

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "OTP sent successfully"));
    }

    @PostMapping("/verify-phone-otp")
    public ResponseEntity<ApiResponse<String>> verifyPhoneOtp(
            @RequestBody VerifyOtpDTO request,
            Authentication authentication) {

        String email = authentication.getName();

        numberVerificationService.verifyOtp(
                email,
                request.getOtp());

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Phone verified successfully"));
    }

}
