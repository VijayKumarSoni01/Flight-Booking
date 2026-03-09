package com.doisproject.userpreview.dto;

import lombok.Data;

@Data
public class AdminChangePass {
    private String email;
    private String oldPassword;
    private String newPassword;
}
