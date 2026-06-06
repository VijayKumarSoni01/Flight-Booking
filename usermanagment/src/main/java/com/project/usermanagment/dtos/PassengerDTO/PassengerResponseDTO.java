package com.project.usermanagment.dtos.PassengerDTO;

import java.time.LocalDate;

import com.project.usermanagment.enumFolder.Gender;
import com.project.usermanagment.enumFolder.PassengerType;
import com.project.usermanagment.enumFolder.Title;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PassengerResponseDTO {
    private Long id;
    private Title title;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String nationality;
    private PassengerType passengerType;
    private String passportNumber;
    private LocalDate passportExpiry;
    private String issuingCountry;
}
