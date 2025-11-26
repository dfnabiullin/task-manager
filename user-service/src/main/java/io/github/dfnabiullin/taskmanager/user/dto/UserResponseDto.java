package io.github.dfnabiullin.taskmanager.user.dto;

import java.util.UUID;

public record UserResponseDto(String name, String email, UUID uuid) {
}