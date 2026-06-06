package com.project.usermanagment.dtos.PassengerDTO;

import java.time.LocalDate;

import com.project.usermanagment.enumFolder.Gender;
import com.project.usermanagment.enumFolder.Title;

import lombok.Data;

@Data
public class UpdatePassengerDTO {
     private Title title;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String nationality;
    private String passportNumber;
    private LocalDate passportExpiry;
    private String issuingCountry;
}
