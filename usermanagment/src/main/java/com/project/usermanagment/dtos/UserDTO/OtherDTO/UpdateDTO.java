package com.project.usermanagment.dtos.UserDTO.OtherDTO;

import java.time.LocalDate;

import com.project.usermanagment.enumFolder.Gender;
import com.project.usermanagment.enumFolder.Title;

import jakarta.persistence.Version;
import lombok.Data;

@Data
public class UpdateDTO {

    private Title title;

    private String firstName;
    private String middleName;
    private String lastName;

    private String username;
    private String phoneNumber;
    private String alternatePhone;

    private Gender gender;
    private LocalDate dateOfBirth;
    private String nationality;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pinCode;

    @Version
    private Long version;
}