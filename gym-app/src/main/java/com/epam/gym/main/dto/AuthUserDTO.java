package com.epam.gym.main.dto;

import com.epam.gym.main.model.UserRole;
import lombok.Builder;

@Builder
public record AuthUserDTO(
        String username,
        String password,
        UserRole role,
        boolean isActive
) {
}
