package com.epam.gym.authservice.dto;

import com.epam.gym.authservice.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserMessageDTO {
    private String username;
    private String password;
    private UserRole role;
    private boolean isActive;
    private ActionType actionType; // ADD or DELETE
}
