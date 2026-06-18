package com.project.usermanagment.dtos.UserDTO.OtherDTO;

import com.project.usermanagment.enumFolder.OtpType;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
//--------------------------------------SendOtpRequest DTO--------------------------
public class SendOtpRequest {

    @NotBlank
    private String identifier;
    @NotBlank
    private OtpType otpType;
}
