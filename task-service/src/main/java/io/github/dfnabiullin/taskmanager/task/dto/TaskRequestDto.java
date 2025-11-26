package io.github.dfnabiullin.taskmanager.task.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record TaskRequestDto(UUID assigneeUuid, @NotBlank(message = "{task.description.notblank}") String description) {
}