package io.github.dfnabiullin.taskmanager.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(@NotBlank(message = "{user.name.notblank}") String name,
                             @NotBlank @Email(message = "{user.email.email}") String email) {
}