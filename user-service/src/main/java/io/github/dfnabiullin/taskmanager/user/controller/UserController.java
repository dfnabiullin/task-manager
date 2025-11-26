package io.github.dfnabiullin.taskmanager.user.controller;

import io.github.dfnabiullin.taskmanager.user.dto.UserPatchRequestDto;
import io.github.dfnabiullin.taskmanager.user.dto.UserRequestDto;
import io.github.dfnabiullin.taskmanager.user.dto.UserResponseDto;
import io.github.dfnabiullin.taskmanager.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return service.createUser(userRequestDto);
    }

    @GetMapping("/{uuid}")
    public UserResponseDto getUserByUuid(@PathVariable UUID uuid) {
        return service.getUserByUuid(uuid);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return service.getAllUsers();
    }

    @PutMapping("/{uuid}")
    public UserResponseDto putUserByUuid(@PathVariable UUID uuid, @RequestBody @Valid UserRequestDto userRequestDto) {
        return service.putUserByUuid(uuid, userRequestDto);
    }

    @PatchMapping("/{uuid}")
    public UserResponseDto patchUserByUuid(@PathVariable UUID uuid, @RequestBody @Valid UserPatchRequestDto userPatchRequestDto) {
        return service.patchUserByUuid(uuid, userPatchRequestDto);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByUuid(@PathVariable UUID uuid) {
        service.deleteUserByUuid(uuid);
    }
}