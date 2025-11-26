package io.github.dfnabiullin.taskmanager.task.dto;

import java.util.UUID;

public record TaskResponseDto(UUID uuid, UUID assigneeUuid, String description) {
}
