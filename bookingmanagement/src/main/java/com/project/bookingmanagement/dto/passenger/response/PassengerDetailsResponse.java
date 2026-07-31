package com.project.bookingmanagement.dto.passenger.response;

import java.time.LocalDate;

import com.project.bookingmanagement.enums.bookingPassangerEnum.Gender;
import com.project.bookingmanagement.enums.bookingPassangerEnum.MealPreference;
import com.project.bookingmanagement.enums.bookingPassangerEnum.PassengerType;
import com.project.bookingmanagement.enums.bookingPassangerEnum.SeatPreference;
import com.project.bookingmanagement.enums.bookingPassangerEnum.Title;

import lombok.Data;

@Data
public class PassengerDetailsResponse {

    private Long passengerId;

    private Title title;

    private String firstName;

    private String middleName;

    private String lastName;

    private PassengerType passengerType;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String nationality;

    private String passportNumber;

    private LocalDate passportExpiry;

    private String passportIssuingCountry;

    private String seatNumber;

    private SeatPreference seatPreference;

    private MealPreference mealPreference;

    private String specialAssistance;
}