package io.github.dfnabiullin.taskmanager.task.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TaskNotFoundException extends RuntimeException {
    private final UUID uuid;

    public TaskNotFoundException(UUID uuid) {
        this.uuid = uuid;
    }
}