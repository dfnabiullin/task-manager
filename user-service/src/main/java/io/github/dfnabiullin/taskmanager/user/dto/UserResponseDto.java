package io.github.dfnabiullin.taskmanager.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UserResponseDto(@Schema(description = "Username", example = "John Doe") String name,
                              @Schema(description = "User's email address", example = "john.doe@example.com") String email,
                              @Schema(description = "User's UUID", example = "123e4567-e89b-12d3-a456-426614174000") UUID uuid) {
}