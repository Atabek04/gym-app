package com.epam.gym.main.dto;

import com.epam.gym.main.model.UserRole;
import com.epam.gym.trainingreport.model.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDTO {
    private String username;
    private String password;
    private UserRole role;
    private boolean isActive;
    private ActionType actionType;
}
