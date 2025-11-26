package io.github.dfnabiullin.taskmanager.user.repository;

import io.github.dfnabiullin.taskmanager.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UUID uuid);
}