package com.project.usermanagment.service.UserService;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
        public void sendResetPasswordEmail(String email, String token) {
        System.out.println("RESET LINK GENERATED:");
        System.out.println("http://localhost:3000/reset-password?token=" + token);
    }
}
