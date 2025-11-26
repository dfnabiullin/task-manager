package io.github.dfnabiullin.taskmanager.user.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final UUID uuid;

    public UserNotFoundException(UUID uuid) {
        this.uuid = uuid;
    }
}