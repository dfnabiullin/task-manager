package io.github.dfnabiullin.taskmanager.user.controller;

import io.github.dfnabiullin.taskmanager.user.dto.UserPatchRequestDto;
import io.github.dfnabiullin.taskmanager.user.dto.UserRequestDto;
import io.github.dfnabiullin.taskmanager.user.dto.UserResponseDto;
import io.github.dfnabiullin.taskmanager.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Methods for working with users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @Operation(summary = "Creating a new user")
    @ApiResponse(responseCode = "201", description = "The user was successfully created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return service.createUser(userRequestDto);
    }

    @Operation(summary = "Getting a user by UUID")
    @ApiResponse(responseCode = "200", description = "The user was found")
    @GetMapping("/{uuid}")
    public UserResponseDto getUserByUuid(@PathVariable UUID uuid) {
        return service.getUserByUuid(uuid);
    }

    @Operation(summary = "Getting a list of all users")
    @ApiResponse(responseCode = "200", description = "The user was found")
    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return service.getAllUsers();
    }

    @Operation(summary = "Full user update")
    @ApiResponse(responseCode = "200", description = "The user has been successfully updated")
    @PutMapping("/{uuid}")
    public UserResponseDto putUserByUuid(@PathVariable UUID uuid, @RequestBody @Valid UserRequestDto userRequestDto) {
        return service.putUserByUuid(uuid, userRequestDto);
    }

    @Operation(summary = "Partial user update")
    @ApiResponse(responseCode = "200", description = "The user has been partially updated successfully")
    @PatchMapping("/{uuid}")
    public UserResponseDto patchUserByUuid(@PathVariable UUID uuid, @RequestBody @Valid UserPatchRequestDto userPatchRequestDto) {
        return service.patchUserByUuid(uuid, userPatchRequestDto);
    }

    @Operation(summary = "Deleting a user")
    @ApiResponse(responseCode = "204", description = "The user has been deleted")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByUuid(@PathVariable UUID uuid) {
        service.deleteUserByUuid(uuid);
    }
}