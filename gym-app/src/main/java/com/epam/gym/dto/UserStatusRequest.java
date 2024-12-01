package com.epam.gym.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserStatusRequest(
        @NotNull(message = "isActive field is required")
        @Schema(example = "true")
        Boolean isActive
) {
}
