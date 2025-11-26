package io.github.dfnabiullin.taskmanager.task.repository;

import io.github.dfnabiullin.taskmanager.task.model.Task;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {

    @Test
    void onBeforeCreate_whenUuidIsNull_shouldGenerateUuid() {
        final var task = new Task();
        task.setUuid(null);

        task.onBeforeCreate();

        assertNotNull(task.getUuid());
    }

    @Test
    void onBeforeCreate_whenUuidIsPresent_shouldKeepExistingUuid() {
        final UUID existingUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        final var task = new Task();
        task.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        task.onBeforeCreate();

        assertEquals(existingUuid, task.getUuid());
    }
}