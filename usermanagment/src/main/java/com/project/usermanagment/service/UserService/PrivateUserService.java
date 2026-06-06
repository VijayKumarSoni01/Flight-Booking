package com.project.usermanagment.service.UserService;

import java.time.LocalDateTime;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.usermanagment.dtos.UserDTO.OtherDTO.UpdateDTO;
import com.project.usermanagment.dtos.UserDTO.passwordDTO.ChangePassRequest;
import com.project.usermanagment.entity.User;

import com.project.usermanagment.repository.UserRepository;
// import com.project.usermanagment.security.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrivateUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // private final JwtUtil jwtUtil;

    public User updateProfile(String email, UpdateDTO update) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // user.setVersion(update.getVersion());

        boolean hashFirst = update.getFirstName() != null;
        boolean hashLast = update.getLastName() != null;

        if (hashFirst || hashLast) {
            if (!(hashFirst && hashLast)) {
                throw new IllegalArgumentException("Both first name and last name must be provided together");
            }
            user.setFirstName(update.getFirstName());
            user.setLastName(update.getLastName());
            user.setMiddleName(update.getMiddleName());
        }

        if (update.getUsername() != null &&
                !update.getUsername().equals(user.getUsername())) {

            if (userRepository.existsByUsername(update.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            user.setUsername(update.getUsername());
        }

        if (update.getPhoneNumber() != null) {
            String normalized = normalizePhone(update.getPhoneNumber());

            if (!normalized.equals(user.getPhoneNumber())) {
                if (userRepository.existsByPhoneNumber(normalized)) {
                    throw new IllegalArgumentException("Phone number already in use");
                }
                user.setPhoneNumber(normalized);
            }
        }

        if (update.getAlternatePhone() != null) {
            String normalizedAlt = normalizePhone(update.getAlternatePhone());
            user.setAlternatePhone(normalizedAlt);
        }

        if (update.getTitle() != null)
            user.setTitle(update.getTitle());

        if (update.getGender() != null)
            user.setGender(update.getGender());

        if (update.getDateOfBirth() != null)
            user.setDateOfBirth(update.getDateOfBirth());

        if (update.getNationality() != null)
            user.setNationality(update.getNationality());

        if (update.getAddressLine1() != null)
            user.setAddressLine1(update.getAddressLine1());

        if (update.getAddressLine2() != null)
            user.setAddressLine2(update.getAddressLine2());

        if (update.getCity() != null)
            user.setCity(update.getCity());

        if (update.getState() != null)
            user.setState(update.getState());

        if (update.getCountry() != null)
            user.setCountry(update.getCountry());

        if (update.getPinCode() != null)
            user.setPinCode(update.getPinCode());

        user.setUpdatedAt(LocalDateTime.now());

        try {
            return userRepository.save(user);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("Profile already updated by another request");
        }
    }

    private String normalizePhone(String phone) {

        if (phone == null || phone.isBlank()) {
            return null;
        }
        phone = phone.replaceAll("[^0-9+]", "").trim();

        if (phone.matches("^\\+91[6-9]\\d{9}$")) {
            return phone;
        }

        if (phone.matches("^[6-9]\\d{9}$")) {
            return "+91" + phone;
        }

        throw new IllegalArgumentException("Invalid phone number format");
    }

    public void changePassword(String email, ChangePassRequest req) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        if (passwordEncoder.matches(req.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as old password");
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

    }

    @Transactional
    public void deactivateCurrentUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new IllegalArgumentException("User already deactivated");
        }

        user.setActive(false);
        user.setDeletedAt(LocalDateTime.now());
        user.setDeletedBy(user.getId());

        userRepository.save(user);
    }

}
