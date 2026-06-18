package com.project.usermanagment.dtos.UserDTO.OtherDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpRequest {
    @NotBlank
    private String identifier;

    @NotBlank
    private String otp;

    @NotBlank
    private String newPassword;
}
