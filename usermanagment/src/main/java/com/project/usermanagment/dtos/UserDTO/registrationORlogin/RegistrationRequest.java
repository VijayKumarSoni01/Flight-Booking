package com.project.usermanagment.dtos.UserDTO.registrationORlogin;

import java.time.LocalDate;

import com.project.usermanagment.enumFolder.Gender;
import com.project.usermanagment.enumFolder.Title;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationRequest {

    private Title title;

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$", message = "Password must be at least 8 characters with letter, number and special character")
    private String password;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9](?:[a-zA-Z0-9._]{4,28}[a-zA-Z0-9])$", message = "Invalid username")
    private String username;

    @NotBlank
    @Pattern(regexp = "^(\\+91)?[6-9][0-9]{9}$", message = "Phone must be valid Indian number")
    private String phoneNumber;

    @Pattern(regexp = "^(\\+91)?[6-9][0-9]{9}$", message = "Alternate phone must be valid Indian number")
    private String alternatePhone;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank
    private String nationality;

    private String addressLine1;
    private String addressLine2;

    private String city;
    private String state;
    private String country;
    private String pinCode;
}