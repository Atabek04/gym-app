package com.epam.gym.authservice.dto;

import com.epam.gym.authservice.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record AuthUserDTO(
        @NotBlank
        @Length(min = 2)
        String username,

        @NotBlank
        @Length(min = 6)
        String password,

        @NotNull
        UserRole role,

        @NotNull
        Boolean isActive
) {
}
