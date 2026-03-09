package com.doisproject.userpreview.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.doisproject.userpreview.dto.RefreshTokenRequest;

import com.doisproject.userpreview.Security.JwtUtil;
import com.doisproject.userpreview.ServiceLayer.UserService;
import com.doisproject.userpreview.dto.ChangePassword;
import com.doisproject.userpreview.dto.LoginRequest;
import com.doisproject.userpreview.dto.UserRegister;
import com.doisproject.userpreview.dto.UserUpdate;
import com.doisproject.userpreview.model.User;

import com.doisproject.userpreview.dto.ApiResponse;
import jakarta.validation.Valid;
import com.doisproject.userpreview.dto.UserAuthResponce;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserAuthResponce>> register(@Valid @RequestBody UserRegister request) {

        UserAuthResponce authResponce = service.register(request);
        return ResponseEntity.ok(ApiResponse.success(authResponce, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserAuthResponce>> login(@Valid @RequestBody LoginRequest request) {

        UserAuthResponce authResponce = service.login(request);
        return ResponseEntity.ok(ApiResponse.success(authResponce, "User logged in successfully"));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<User>> update(@Valid @RequestBody UserUpdate request, Authentication auth) {
        User updated = service.update(auth.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(updated, "User updated successfully"));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePassword request,
            Authentication auth) {
        service.changePassword(auth.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<UserAuthResponce>>refresh(@Valid @RequestBody RefreshTokenRequest request) {
    String refreshToken = request.getRefreshToken(); 
        String email = jwtUtil.extractEmail(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if(jwtUtil.validateToken(refreshToken, userDetails)){
            String newToken = jwtUtil.generateToken(userDetails);
            String newRefresh = jwtUtil.generateRefreshToken(userDetails);

            return ResponseEntity.ok(
                    ApiResponse.success(
                            UserAuthResponce.builder()
                                    .token(newToken)
                                    .refreshToken(newRefresh)
                                    .build(),
                            "Token refreshed"
                    )
            );
        }
        throw new IllegalArgumentException("Invalid refresh token");
    }

}
