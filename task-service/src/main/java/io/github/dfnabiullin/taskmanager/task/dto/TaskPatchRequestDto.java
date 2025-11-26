package io.github.dfnabiullin.taskmanager.task.dto;

import java.util.UUID;

public record TaskPatchRequestDto(UUID assigneeUuid, String description) {
}