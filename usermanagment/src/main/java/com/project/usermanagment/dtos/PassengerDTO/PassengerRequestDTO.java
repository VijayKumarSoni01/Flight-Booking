package com.project.usermanagment.dtos.PassengerDTO;

import java.time.LocalDate;

import com.project.usermanagment.enumFolder.Gender;
import com.project.usermanagment.enumFolder.Title;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

@Data
public class PassengerRequestDTO {

    @NotNull
    private Title title;

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    @NotNull
    @Past
    private LocalDate dateOfBirth;

    @NotNull
    private Gender gender;

    @NotBlank
    private String nationality;


    private String passportNumber;
    private LocalDate passportExpiry;
    private String issuingCountry;

}
