package io.github.dfnabiullin.taskmanager.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
        @Schema(description = "Username", example = "John Doe") @NotBlank(message = "{user.name.notblank}") String name,
        @Schema(description = "User's email address", example = "john.doe@example.com") @NotBlank @Email(message = "{user.email.email}") String email) {
}