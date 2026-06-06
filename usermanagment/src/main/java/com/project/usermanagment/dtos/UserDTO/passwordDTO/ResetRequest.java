package com.project.usermanagment.dtos.UserDTO.passwordDTO;

import lombok.Data;

@Data
public class ResetRequest {
    private String token;
    private String newPassword;
}
