package io.github.dfnabiullin.taskmanager.service;

import io.github.dfnabiullin.taskmanager.model.User;
import io.github.dfnabiullin.taskmanager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Primary
public class UserService {
    private final UserRepository repository;

    public User createUser(User user) {
        return repository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }
}
