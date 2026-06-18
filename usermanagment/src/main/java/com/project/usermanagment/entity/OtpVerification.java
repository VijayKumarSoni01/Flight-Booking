package com.project.usermanagment.entity;

import com.project.usermanagment.enumFolder.OtpType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class OtpVerification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String otp;
    private long expirationTime;
    private boolean isVerified;

    @Enumerated(EnumType.STRING)
    private OtpType otpType;

}
