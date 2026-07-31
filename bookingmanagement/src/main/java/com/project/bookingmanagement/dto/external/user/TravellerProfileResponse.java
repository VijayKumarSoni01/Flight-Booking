package com.project.bookingmanagement.dto.external.user;

import java.time.LocalDate;

import com.project.bookingmanagement.enums.bookingPassangerEnum.Gender;
import com.project.bookingmanagement.enums.bookingPassangerEnum.PassengerType;
import com.project.bookingmanagement.enums.bookingPassangerEnum.Title;

import lombok.Data;

@Data
public class TravellerProfileResponse {

    private Long travellerId;

    private Title title;

    private String firstName;

    private String middleName;

    private String lastName;

    private LocalDate dateOfBirth;

    private Gender gender;

    private PassengerType passengerType;

    private String nationality;

    private String passportNumber;

    private LocalDate passportExpiry;

    private String passportIssuingCountry;
}