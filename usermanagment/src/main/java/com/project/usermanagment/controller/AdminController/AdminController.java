package com.project.usermanagment.controller.AdminController;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.project.usermanagment.dtos.UserDTO.securitydto.ApiResponse;
import com.project.usermanagment.entity.User;
import com.project.usermanagment.enumFolder.Role;
import com.project.usermanagment.service.AdminService.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminController {

    private final AdminService service;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<User>>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String dir) {

        log.info("Fetching users | search={} role={} page={} size={}", search, role, page, size);

        return ResponseEntity.ok(
                ApiResponse.success(
                        service.getUsers(search, role, page, size, sortBy, dir),
                        "Users fetched"));
    }

    @PatchMapping("/promote/{id}")
    public ResponseEntity<ApiResponse<String>> promoteUser(
            @PathVariable Long id,
            Authentication auth) {

        String currentEmail = auth.getName();

        service.promoteToAdmin(id, currentEmail);

        return ResponseEntity.ok(
                ApiResponse.success(null, "User promoted to ADMIN"));
    }

    @PatchMapping("/demote/{id}")
    public ResponseEntity<ApiResponse<String>> demoteUser(
            @PathVariable Long id,
            Authentication auth) {

        String currentEmail = auth.getName();

        service.demoteToUser(id, currentEmail);

        return ResponseEntity.ok(
                ApiResponse.success(null, "User demoted to USER"));
    }

    @PatchMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<String>> restoreUser(@PathVariable Long id) {

        log.info("Restoring user with id: {}", id);

        service.restoreUser(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "User restored successfully"));
    }

    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @PathVariable Long id,
            Authentication auth) {

        String currentEmail = auth.getName();

        log.info("Hard deleting user {} by admin {}", id, currentEmail);

        service.hardDeleteUser(id, currentEmail);

        return ResponseEntity.ok(
                ApiResponse.success(null, "User permanently deleted"));
    }
}