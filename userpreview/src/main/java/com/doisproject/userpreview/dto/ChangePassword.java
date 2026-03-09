package com.doisproject.userpreview.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword {
    @NotBlank
    private String oldPassword;
    @NotBlank @Size(min = 6)
    private String newPassword;
}
