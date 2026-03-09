package com.doisproject.userpreview.ServiceLayer;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.doisproject.userpreview.dto.AdminAuthResponse;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doisproject.userpreview.Security.JwtUtil;
import com.doisproject.userpreview.dto.AdminChangePass;
import com.doisproject.userpreview.dto.AdminLogin;
import com.doisproject.userpreview.dto.AdminRegister;
import com.doisproject.userpreview.dto.AdminUpdate;
import com.doisproject.userpreview.dto.UserStatus;

import com.doisproject.userpreview.model.Admin;
import com.doisproject.userpreview.model.User;
import com.doisproject.userpreview.repository.AdminRepository;
import com.doisproject.userpreview.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AdminAuthResponse register(AdminRegister request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Admin admin = Admin.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .build();

        Admin savedAdmin = repository.save(admin);
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedAdmin.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails); // add this
        return AdminAuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .userId(savedAdmin.getId())
                .email(savedAdmin.getEmail())
                .firstName(savedAdmin.getFirstName())
                .lastName(savedAdmin.getLastName())
                .build();
    }

    public AdminAuthResponse login(AdminLogin request) {
        Admin admin = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (admin.getLockedUntil() != null && admin.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Account is locked until " + admin.getLockedUntil());
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            admin.setFailedLoginAttempts(0);
            admin.setLockedUntil(null);
            admin.setLastLoginAt(LocalDateTime.now());
            repository.save(admin);

            UserDetails userDetails = userDetailsService.loadUserByUsername(admin.getEmail());
            String token = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            return AdminAuthResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .email(admin.getEmail())
                    .firstName(admin.getFirstName())
                    .lastName(admin.getLastName())
                    .build();

        } catch (Exception e) {
            admin.setFailedLoginAttempts(admin.getFailedLoginAttempts() + 1);
            if (admin.getFailedLoginAttempts() >= 3) {
                admin.setLockedUntil(LocalDateTime.now().plusMinutes(15));
            }
            repository.save(admin);
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public Admin update(String email, AdminUpdate request) {
        Admin admin = repository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        if (request.getFirstName() != null)
            admin.setFirstName(request.getFirstName());
        if (request.getMiddleName() != null)
            admin.setMiddleName(request.getMiddleName());
        if (request.getLastName() != null)
            admin.setLastName(request.getLastName());
        if (request.getPhone() != null)
            admin.setPhone(request.getPhone());
        if (request.getDateOfBirth() != null)
            admin.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));

        admin.setUpdatedAt(LocalDateTime.now());
        return repository.save(admin);
    }

    public String changePassword(String email, AdminChangePass request) {

        Admin admin = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        admin.setUpdatedAt(LocalDateTime.now());

        repository.save(admin);

        return "Password changed successfully";
    }

    // @DeleteMapping("/delete/{id}")
    // public String deleteUserByAdmin(@PathVariable String id) {

    // if (!userRepository.existsById(id)) {
    // throw new IllegalArgumentException("User not found");
    // }

    // userRepository.deleteById(id);

    // return "User deleted successfully";
    // }

    public UserStatus deactivateUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setIsActive(false);
        user.getIsActive();
        user.setDeactivatedAt(LocalDateTime.now());
        userRepository.save(user);

        return UserStatus.builder()
                .id(user.getId())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .message("User account deactivated")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public UserStatus activateUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setIsActive(true);
        user.setDeactivatedAt(null);
        userRepository.save(user);

        return UserStatus.builder()
                .id(user.getId())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .message("User account activated")
                .timestamp(LocalDateTime.now())
                .build();
    }

}
