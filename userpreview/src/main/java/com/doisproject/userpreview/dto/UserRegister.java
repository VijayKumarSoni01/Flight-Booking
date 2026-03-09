package com.doisproject.userpreview.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegister {

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6)
    @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
    message = "Password must have 6+ chars, uppercase, lowercase, number and special character"
)
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String passportNumber;
    private String dateOfBirth;
    private String nationality;

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate lastLoginAt;
}

