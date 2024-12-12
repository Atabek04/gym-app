package com.epam.gym.authservice.dto;

import com.epam.gym.authservice.model.UserRole;
import lombok.Builder;

@Builder
public record AuthUserDTO(
        String username,
        String password,
        UserRole role,
        boolean isActive
) {
}
