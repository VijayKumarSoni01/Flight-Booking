package com.project.usermanagment.service.UserService;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.project.usermanagment.config.twilioprop.OtpProperties;
import com.project.usermanagment.config.twilioprop.TwilioProperties;
import com.project.usermanagment.entity.User;
import com.project.usermanagment.repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NumberVerificationService {

    private final UserRepository userRepo;
    private final TwilioProperties twilioProps;
    private final OtpProperties otpProperties;

    @PostConstruct
public void initTwilio() {

    log.info("Twilio initialized successfully");

    Twilio.init(
            twilioProps.getAccountSid(),
            twilioProps.getAuthToken());
}

    public void sendOtp(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.isPhoneVerified())) {
            throw new IllegalStateException(
                    "Phone number already verified");
        }

        String otp = generateOtp();

        user.setPhoneOtp(otp);

        user.setOtpExpiry(
                LocalDateTime.now()
                        .plusMinutes(
                                otpProperties.getExpiryMinutes()));

        userRepo.save(user);

        sendSms(
                user.getPhoneNumber(),
                buildOtpMessage(otp));

        log.info("OTP sent to {}", user.getPhoneNumber());
    }

    public void verifyOtp(String email, String otp) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.isPhoneVerified())) {
            throw new IllegalStateException(
                    "Phone number already verified");
        }

        if (user.getOtpExpiry() == null
                || user.getOtpExpiry().isBefore(LocalDateTime.now())) {

            throw new IllegalStateException(
                    "OTP has expired");
        }

        if (!user.getPhoneOtp().equals(otp.trim())) {
            throw new IllegalArgumentException(
                    "Invalid OTP");
        }

        user.setPhoneVerified(true);
        user.setPhoneOtp(null);
        user.setOtpExpiry(null);

        userRepo.save(user);

        log.info("Phone verified for user {}", user.getEmail());
    }

    public void resendOtp(String email) {
        sendOtp(email);
    }

    private void sendSms(String toPhone, String messageBody) {

    try {

        Message.creator(
                new PhoneNumber(toPhone),
                new PhoneNumber(twilioProps.getPhoneNumber()),
                messageBody
        ).create();

    } catch (Exception e) {

        e.printStackTrace();

        throw new RuntimeException(
                "Twilio Error: " + e.getMessage());
    }
}

    private String generateOtp() {

        Random random = new Random();

        int min = (int) Math.pow(
                10,
                otpProperties.getLength() - 1);

        int max = (int) Math.pow(
                10,
                otpProperties.getLength()) - 1;

        int otp = random.nextInt(max - min + 1) + min;

        return String.valueOf(otp);
    }

    private String buildOtpMessage(String otp) {

        return "Your FlightBooking verification code is: "
                + otp
                + ". Valid for "
                + otpProperties.getExpiryMinutes()
                + " minutes.";
    }
}