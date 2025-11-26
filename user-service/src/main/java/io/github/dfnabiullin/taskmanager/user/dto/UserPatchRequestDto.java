package io.github.dfnabiullin.taskmanager.user.dto;

import jakarta.validation.constraints.Email;

public record UserPatchRequestDto(String name, @Email(message = "{user.email.email}") String email) {
}