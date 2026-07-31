package com.project.bookingmanagement.dto.external.user;

import lombok.Data;

@Data
public class UserProfileResponse {

    private Long userId;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private boolean emailVerified;

    private boolean accountActive;
}
