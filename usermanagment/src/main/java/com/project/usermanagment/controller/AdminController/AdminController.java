package com.project.usermanagment.controller.AdminController;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.usermanagment.dtos.UserDTO.securitydto.ApiResponse;
import com.project.usermanagment.entity.User;
import com.project.usermanagment.enumFolder.Role;
import com.project.usermanagment.service.AdminService.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Get Users", description = "Get users with pagination, search, role filter and sorting", tags = {
            "Admin APIs" })
    @GetMapping
    public ResponseEntity<ApiResponse<Page<User>>> getUsers(

            @RequestParam(required = false) String search,

            @RequestParam(required = false) Role role,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "desc") String dir) {

        Page<User> users = adminService.getUsers(
                search,
                role,
                page,
                size,
                sortBy,
                dir);

        return ResponseEntity.ok(
                ApiResponse.success(
                        users,
                        "Users fetched successfully"));
    }

    @Operation(summary = "Promote User", description = "Grant ADMIN role to a user", tags = { "Admin APIs" })
    @PatchMapping("/{id}/promote")
    public ResponseEntity<ApiResponse<String>> promoteUser(

            @PathVariable Long id,
            Authentication authentication) {

        adminService.promoteToAdmin(
                id,
                authentication.getName());

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "User promoted to ADMIN successfully"));
    }

    @Operation(summary = "Demote Admin", description = "Remove ADMIN role from a user", tags = { "Admin APIs" })
    @PatchMapping("/{id}/demote")
    public ResponseEntity<ApiResponse<String>> demoteUser(

            @PathVariable Long id,
            Authentication authentication) {

        adminService.demoteToUser(
                id,
                authentication.getName());

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "ADMIN role removed successfully"));
    }

    @Operation(summary = "Restore User", description = "Restore a deactivated user account", tags = { "Admin APIs" })
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<String>> restoreUser(
            @PathVariable Long id) {

        adminService.restoreUser(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "User restored successfully"));
    }

    @Operation(summary = "Hard Delete User", description = "Permanently delete a deactivated user", tags = {
            "Admin APIs" })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> hardDeleteUser(

            @PathVariable Long id,
            Authentication authentication) {

        adminService.hardDeleteUser(
                id,
                authentication.getName());

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "User deleted permanently"));
    }
}