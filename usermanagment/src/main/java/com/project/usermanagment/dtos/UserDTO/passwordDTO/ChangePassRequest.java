package com.project.usermanagment.dtos.UserDTO.passwordDTO;

import lombok.Data;

@Data
public class ChangePassRequest {
    private String oldPassword;
    private String newPassword;
}
