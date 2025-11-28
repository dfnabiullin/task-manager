package io.github.dfnabiullin.taskmanager.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record UserPatchRequestDto(@Schema(description = "Username", example = "John Doe") String name,
                                  @Schema(description = "User's email address", example = "john.doe@example.com") @Email(message = "{user.email.email}") String email) {
}