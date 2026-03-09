package com.doisproject.userpreview.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminRegister {

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
    message = "Password must have 8+ chars, uppercase, lowercase, number and special character"
)
    private String password;

     @NotBlank(message = "First name must not be empty")
    private String firstName;
    private String middleName;

    @NotBlank(message = "Last name must not be empty")
    private String lastName;

    @NotBlank(message = "Phone must not be empty")  
    private String phone;

    @NotNull(message = "Date of birth must not be empty")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
}