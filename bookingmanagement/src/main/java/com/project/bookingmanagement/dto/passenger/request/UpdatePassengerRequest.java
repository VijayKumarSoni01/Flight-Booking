package com.project.bookingmanagement.dto.passenger.request;



import com.project.bookingmanagement.enums.bookingPassangerEnum.MealPreference;
import com.project.bookingmanagement.enums.bookingPassangerEnum.SeatPreference;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePassengerRequest {

    private MealPreference mealPreference;

    private SeatPreference seatPreference;

    @Size(max = 300, message = "Special assistance cannot exceed 300 characters")
    private String specialAssistance;
}