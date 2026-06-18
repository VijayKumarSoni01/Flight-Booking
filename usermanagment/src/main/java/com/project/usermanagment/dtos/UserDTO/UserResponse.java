package com.project.usermanagment.dtos.UserDTO;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified;
}