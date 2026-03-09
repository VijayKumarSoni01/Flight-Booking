package com.doisproject.userpreview.controller;

import com.doisproject.userpreview.dto.AdminAuthResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doisproject.userpreview.ServiceLayer.AdminService;
import com.doisproject.userpreview.dto.AdminChangePass;
import com.doisproject.userpreview.dto.AdminLogin;
import com.doisproject.userpreview.dto.AdminRegister;
import com.doisproject.userpreview.dto.AdminUpdate;
import com.doisproject.userpreview.dto.ApiResponse;
import com.doisproject.userpreview.dto.UserStatus;
import com.doisproject.userpreview.model.Admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService service;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AdminAuthResponse>> register(@Valid @RequestBody AdminRegister request) {

        AdminAuthResponse authResponse = service.register(request);
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Admin registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminAuthResponse>> login(@Valid @RequestBody AdminLogin request) {

        AdminAuthResponse authResponse = service.login(request);
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Admin logged in successfully"));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Admin>> update(@Valid @RequestBody AdminUpdate request, Authentication auth) {
        Admin updated = service.update(auth.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Admin updated successfully"));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody AdminChangePass request,
            Authentication auth) {
        service.changePassword(auth.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }

    // @DeleteMapping("/delete-user/{userId}")
    // public Map<String, String> deleteUser(@PathVariable String userId) {

    // String result = service.deleteUserByAdmin(userId);

    // return Map.of("message", result);
    // }

    @PatchMapping("/deactivate-user/{email}")
    public ResponseEntity<ApiResponse<UserStatus>> deactivateUser(@PathVariable String email) {
        UserStatus status = service.deactivateUser(email);
        return ResponseEntity.ok(ApiResponse.success(status, "User deactivated successfully"));
    }

    @PatchMapping("/activate-user/{email}")
    public ResponseEntity<ApiResponse<UserStatus>> activateUser(@PathVariable String email) {
        UserStatus status = service.activateUser(email);
        return ResponseEntity.ok(ApiResponse.success(status, "User activated successfully"));
    }
}
