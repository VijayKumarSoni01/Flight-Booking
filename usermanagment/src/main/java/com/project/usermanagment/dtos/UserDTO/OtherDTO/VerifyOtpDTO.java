package com.project.usermanagment.dtos.UserDTO.OtherDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyOtpDTO {

    @NotNull
    private String email;

    @NotBlank
    private String otp;
}
