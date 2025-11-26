package io.github.dfnabiullin.taskmanager.user.repository;

import io.github.dfnabiullin.taskmanager.user.model.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {

    @Test
    void onBeforeCreate_whenUuidIsNull_shouldGenerateUuid() {
        final var user = new User();
        user.setUuid(null);

        user.onBeforeCreate();

        assertNotNull(user.getUuid());
    }

    @Test
    void onBeforeCreate_whenUuidIsPresent_shouldKeepExistingUuid() {
        final UUID existingUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        final var user = new User();
        user.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        user.onBeforeCreate();

        assertEquals(existingUuid, user.getUuid());
    }
}