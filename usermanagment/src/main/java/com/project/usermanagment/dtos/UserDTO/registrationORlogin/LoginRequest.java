package com.project.usermanagment.dtos.UserDTO.registrationORlogin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email/Username/Phone is required")
    private String identifier;
    @NotBlank(message = "Password is required")
    private String password;
}
