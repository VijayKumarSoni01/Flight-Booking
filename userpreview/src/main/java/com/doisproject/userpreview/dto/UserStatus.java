package com.doisproject.userpreview.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus {

    private String id;
    private String email;
    private boolean isActive;
    private String message;
     private LocalDateTime timestamp;
}