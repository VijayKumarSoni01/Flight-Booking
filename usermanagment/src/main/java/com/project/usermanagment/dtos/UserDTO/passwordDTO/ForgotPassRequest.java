package com.project.usermanagment.dtos.UserDTO.passwordDTO;

import com.project.usermanagment.enumFolder.OtpType;

import lombok.Data;

@Data
public class ForgotPassRequest {

    private String identifier;

    private OtpType otpType;
}