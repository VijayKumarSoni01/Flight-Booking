package com.project.usermanagment.dtos.UserDTO.securitydto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponse {
    private String token;
    private String accessToken;
    private String refreshToken;
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
}
