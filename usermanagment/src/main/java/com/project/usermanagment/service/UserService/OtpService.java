package com.project.usermanagment.service.UserService;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    public String generateOtp() {

        return String.format(
                "%06d",
                ThreadLocalRandom.current()
                        .nextInt(1000000));
    }
}