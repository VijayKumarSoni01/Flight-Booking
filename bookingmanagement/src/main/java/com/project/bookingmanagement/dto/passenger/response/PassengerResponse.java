package com.project.bookingmanagement.dto.passenger.response;

import java.time.LocalDate;

import com.project.bookingmanagement.enums.bookingPassangerEnum.Gender;
import com.project.bookingmanagement.enums.bookingPassangerEnum.MealPreference;
import com.project.bookingmanagement.enums.bookingPassangerEnum.PassengerType;
import com.project.bookingmanagement.enums.bookingPassangerEnum.SeatPreference;
import com.project.bookingmanagement.enums.bookingPassangerEnum.Title;

import lombok.Data;

@Data
public class PassengerResponse {

    private Long passengerId;

    private Title title;

    private String firstName;

    private String middleName;

    private String lastName;

    private PassengerType passengerType;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String nationality;

    private MealPreference mealPreference;

    private SeatPreference seatPreference;

    private String seatNumber;

    private String specialAssistance;
}