package com.project.usermanagment.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.usermanagment.dtos.UserDTO.registrationORlogin.RegistrationRequest;
import com.project.usermanagment.dtos.UserDTO.securitydto.UserAuthResponse;
import com.project.usermanagment.entity.User;
import com.project.usermanagment.enumFolder.OtpType;
import com.project.usermanagment.repository.UserRepository;
import com.project.usermanagment.security.JwtUtil;
import com.project.usermanagment.service.UserService.Verification.EmailVerificationService;
import com.project.usermanagment.service.UserService.Verification.NumberVerificationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailVerificationService emailService;
    private final NumberVerificationService numberVerificationService;
    private final OtpService otpService;

    public UserAuthResponse login(String identifier, String password, String ipAddress) {

        String login = identifier.trim().toLowerCase();

        if (login.matches("^[6-9][0-9]{9}$")) {
            login = "+91" + login;
        }

        if (login.contains("@")) {
            login = login.toLowerCase();
        }

        User user = userRepository
                .findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.isActive()) {
            throw new IllegalArgumentException("User account is inactive");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        user.setLastLogin(LocalDateTime.now());
        user.setLastLoginIp(ipAddress);
        user.setLoginCount(user.getLoginCount() + 1);

        log.info(
                "Updating login info | User={} | Count={} | IP={}",
                user.getEmail(),
                user.getLoginCount(),
                ipAddress);

        userRepository.save(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();

        String token = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return UserAuthResponse.builder()
                .userId(String.valueOf(user.getId()))
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public UserAuthResponse register(RegistrationRequest request) {

        String email = request.getEmail().trim().toLowerCase();
        String username = request.getUsername().trim();

        String phone = request.getPhoneNumber().trim();
        if (!phone.startsWith("+91")) {
            phone = "+91" + phone;
        }

        String alternatePhone = request.getAlternatePhone();
        if (alternatePhone != null && !alternatePhone.isBlank()) {
            alternatePhone = alternatePhone.trim();
            if (!alternatePhone.startsWith("+91")) {
                alternatePhone = "+91" + alternatePhone;
            }
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }

        User user = User.builder()
                .title(request.getTitle())
                .firstName(request.getFirstName().trim())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName().trim())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .username(username)
                .phoneNumber(phone)
                .alternatePhone(alternatePhone)
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .nationality(request.getNationality())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .pinCode(request.getPinCode())
                .build();

        User savedUser = userRepository.save(user);

        userRepository.flush();

        System.out.println("Saved User ID = " + savedUser.getId());

        // emailService.sendVerificationEmail(savedUser);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(savedUser.getEmail())
                .password(savedUser.getPassword())
                .authorities("USER")
                .build();

        String token = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return UserAuthResponse.builder()
                .userId(String.valueOf(savedUser.getId()))
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    public void initiatePasswordReset(
            String identifier,
            OtpType otpType) {

        User user;

        if (otpType == OtpType.EMAIL) {

            user = userRepository.findByEmail(identifier)
                    .orElseThrow(
                            () -> new RuntimeException("User not found"));

            if (!user.isEmailVerified()) {

                throw new RuntimeException(
                        "Email is not verified");
            }

        } else {

            user = userRepository.findByPhoneNumber(identifier)
                    .orElseThrow(
                            () -> new RuntimeException("User not found"));

            if (!user.isPhoneVerified()) {

                throw new RuntimeException(
                        "Phone number is not verified");
            }
        }

        String otp = otpService.generateOtp();

        user.setPhoneOtp(otp);
        user.setOtpExpiry(
                LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        if (otpType == OtpType.EMAIL) {

            emailService.sendOtpEmail(
                    user.getEmail(),
                    otp);

        } else {

            numberVerificationService.sendPasswordResetOtp(
                    user.getPhoneNumber(),
                    otp);
        }
    }

    public void resetPassword(
            String email,
            String otp,
            String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("User not found"));

        if (user.getOtpExpiry() == null ||
                user.getOtpExpiry()
                        .isBefore(LocalDateTime.now())) {

            throw new RuntimeException("OTP expired");
        }

        if (!otp.equals(user.getPhoneOtp())) {

            throw new RuntimeException("Invalid OTP");
        }

        user.setPassword(
                passwordEncoder.encode(newPassword));

        user.setPhoneOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);
    }

    public UserAuthResponse refreshAccessToken(String refreshToken) {

        String username = jwtUtil.extractSubject(refreshToken);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtUtil.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user);

        return UserAuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();

    }

    public void generateRestoreToken(String email) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || user.isActive()) {
            return;
        }

        String token = java.util.UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        log.info("Restore token for {}: {}", user.getEmail(), token);

        emailService.sendResetPasswordEmail(user.getEmail(), token);
    }

    @Transactional
    public void restoreWithToken(String token) {

        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry() == null ||
                user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {

            throw new RuntimeException("Token expired");
        }

        if (user.getDeletedAt() == null ||
                user.getDeletedAt().isBefore(LocalDateTime.now().minusDays(30))) {

            throw new RuntimeException("Restore period expired");
        }

        user.setActive(true);
        user.setDeletedAt(null);
        user.setDeletedBy(null);

        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }

    @Transactional
    public void deleteExpiredUsers() {

        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);

        List<User> users = userRepository.findUsersDeletedBefore(cutoff);

        userRepository.deleteAll(users);
    }

    // ---------------------Otp change password--------------------------
    // private String generateOtp() {
    // return String.valueOf(
    // ThreadLocalRandom.current()
    // .nextInt(100000, 999999));
    // }

    // public void sendResetOtp(String email) {

    // User user = userRepository.findByEmail(email)
    // .orElseThrow(() -> new RuntimeException("User not found"));

    // String otp = generateOtp();

    // user.setPhoneOtp(otp);
    // user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

    // userRepository.save(user);

    // emailService.sendOtpEmail(user.getEmail(), otp);
    // }

}
