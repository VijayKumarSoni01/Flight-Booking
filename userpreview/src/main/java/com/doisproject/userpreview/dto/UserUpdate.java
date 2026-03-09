package com.doisproject.userpreview.dto;

import lombok.Data;

@Data
public class UserUpdate {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String passportNumber;
    private String dateOfBirth;
}
