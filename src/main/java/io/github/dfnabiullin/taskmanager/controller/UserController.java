package io.github.dfnabiullin.taskmanager.controller;

import io.github.dfnabiullin.taskmanager.model.User;
import io.github.dfnabiullin.taskmanager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("create_user")
    public User createUser(@RequestBody User user) {
        return service.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return service.getUserById(id).get();
    }

    @GetMapping("get_all_users")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }
}
