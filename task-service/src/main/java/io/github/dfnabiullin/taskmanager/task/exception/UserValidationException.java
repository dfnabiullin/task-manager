package io.github.dfnabiullin.taskmanager.task.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserValidationException extends RuntimeException {
    private final UUID uuid;

    public UserValidationException(UUID uuid) {
        this.uuid = uuid;
    }
}