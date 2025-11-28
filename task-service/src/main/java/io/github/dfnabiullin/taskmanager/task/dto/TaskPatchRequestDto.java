package io.github.dfnabiullin.taskmanager.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record TaskPatchRequestDto(
        @Schema(description = "The UUID of the assigned user", example = "123e4567-e89b-12d3-a456-426614174000") UUID assigneeUuid,
        @Schema(description = "Task description", example = "Processing of nickel silver spoons") String description) {
}