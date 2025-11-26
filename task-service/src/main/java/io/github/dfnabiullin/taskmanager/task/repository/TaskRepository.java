package io.github.dfnabiullin.taskmanager.task.repository;

import io.github.dfnabiullin.taskmanager.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByUuid(UUID uuid);
}