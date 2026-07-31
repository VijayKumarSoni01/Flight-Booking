package com.project.bookingmanagement.dto.passenger.request;

import java.time.LocalDate;

import com.project.bookingmanagement.enums.bookingPassangerEnum.Gender;
import com.project.bookingmanagement.enums.bookingPassangerEnum.MealPreference;
import com.project.bookingmanagement.enums.bookingPassangerEnum.PassengerType;
import com.project.bookingmanagement.enums.bookingPassangerEnum.SeatPreference;
import com.project.bookingmanagement.enums.bookingPassangerEnum.Title;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddPassengerRequest {

    @NotNull(message = "Title is required")
    private Title title;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Size(max = 50, message = "Middle name cannot exceed 50 characters")
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Passenger type is required")
    private PassengerType passengerType;

    @NotBlank(message = "Nationality is required")
    @Size(max = 50, message = "Nationality cannot exceed 50 characters")
    private String nationality;

    @Pattern(
            regexp = "^[A-Z0-9]{6,20}$",
            message = "Invalid passport number")
    private String passportNumber;

    private LocalDate passportExpiry;

    @Size(max = 50, message = "Passport issuing country cannot exceed 50 characters")
    private String passportIssuingCountry;

    private SeatPreference seatPreference;

    private MealPreference mealPreference;

    @Size(max = 300, message = "Special assistance cannot exceed 300 characters")
    private String specialAssistance;
}