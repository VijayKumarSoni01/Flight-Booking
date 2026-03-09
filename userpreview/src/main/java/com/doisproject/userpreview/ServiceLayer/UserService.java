package com.doisproject.userpreview.ServiceLayer;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doisproject.userpreview.Security.JwtUtil;
import com.doisproject.userpreview.dto.ChangePassword;
import com.doisproject.userpreview.dto.LoginRequest;
import com.doisproject.userpreview.dto.UserAuthResponce;
import com.doisproject.userpreview.dto.UserRegister;
import com.doisproject.userpreview.dto.UserUpdate;
import com.doisproject.userpreview.model.User;
import com.doisproject.userpreview.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public UserAuthResponce register(UserRegister request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .passportNumber(request.getPassportNumber())
                .dateOfBirth(LocalDate.parse(request.getDateOfBirth()))
                .nationality(request.getNationality())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

        User savedUser = repository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        return UserAuthResponce.builder()
                .token(token)
                .refreshToken(refreshToken)
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .build();
    }

    public UserAuthResponce login(LoginRequest request) {

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Account is locked until " + user.getLockedUntil());
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            user.setFailedLoginAttempts(0);
            user.setLockedUntil(null);
            user.setLastLoginAt(LocalDateTime.now());
            repository.save(user);

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String token = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            return UserAuthResponce.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .userId(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build();

        } catch (Exception e) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= 3) {
                user.setLockedUntil(LocalDateTime.now().plusMinutes(15));
            }
            repository.save(user);
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public User update(String email, UserUpdate request) {
        {
            User user = repository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
            if (request.getFirstName() != null)
                user.setFirstName(request.getFirstName());
            if (request.getMiddleName() != null)
                user.setMiddleName(request.getMiddleName());
            if (request.getLastName() != null)
                user.setLastName(request.getLastName());
            if (request.getPassportNumber() != null)
                user.setPassportNumber(request.getPassportNumber());
            if (request.getPhone() != null)
                user.setPhone(request.getPhone());
            if (request.getDateOfBirth() != null)
                user.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));

            user.setUpdatedAt(LocalDate.now());
            return repository.save(user);
        }

    }

    public String changePassword(String email, ChangePassword request) {

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDate.now());

        repository.save(user);

        return "Password changed successfully";
    }
}
