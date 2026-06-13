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

    // ✅ FIX line 50: @NonNull tells the IDE this @PathVariable is guaranteed non-null
    @PatchMapping("/promote/{id}")
    public ResponseEntity<ApiResponse<String>> promoteUser(
            @PathVariable Long id,
            Authentication auth) {

        service.promoteToAdmin(id, auth.getName());

        return ResponseEntity.ok(ApiResponse.success(null, "User promoted to ADMIN"));
    }

    // ✅ FIX line 63
    @PatchMapping("/demote/{id}")
    public ResponseEntity<ApiResponse<String>> demoteUser(
            @PathVariable Long id,
            Authentication auth) {

        service.demoteToUser(id, auth.getName());

        return ResponseEntity.ok(ApiResponse.success(null, "User demoted to USER"));
    }

    // ✅ FIX line 74
    @PatchMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<String>> restoreUser(
            @PathVariable Long id) {

        log.info("Restoring user with id: {}", id);
        service.restoreUser(id);

        return ResponseEntity.ok(ApiResponse.success(null, "User restored successfully"));
    }

    // ✅ FIX line 89
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @PathVariable Long id,
            Authentication auth) {

        log.info("Hard deleting user {} by admin {}", id, auth.getName());
        service.hardDeleteUser(id, auth.getName());

        return ResponseEntity.ok(ApiResponse.success(null, "User permanently deleted"));
    }
}