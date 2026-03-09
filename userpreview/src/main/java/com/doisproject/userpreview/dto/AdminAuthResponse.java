package com.doisproject.userpreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthResponse {
    private String token;
    private String refreshToken;
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
}
