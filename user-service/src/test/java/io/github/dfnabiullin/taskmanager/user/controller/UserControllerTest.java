package io.github.dfnabiullin.taskmanager.user.controller;

import io.github.dfnabiullin.taskmanager.user.dto.UserPatchRequestDto;
import io.github.dfnabiullin.taskmanager.user.dto.UserRequestDto;
import io.github.dfnabiullin.taskmanager.user.dto.UserResponseDto;
import io.github.dfnabiullin.taskmanager.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService service;
    @InjectMocks
    private UserController controller;

    @Test
    void createUser_shouldReturnCreatedUser() {
        final var initialUserRequestDto = new UserRequestDto("John Doe", "john.doe@example.com");

        final var createdUser = new UserResponseDto("John Doe", "john.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var expectedUserResponseDto = new UserResponseDto("John Doe", "john.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        when(service.createUser(any(UserRequestDto.class))).thenReturn(createdUser);

        final UserResponseDto actualUserResponseDto = controller.createUser(initialUserRequestDto);

        assertEquals(expectedUserResponseDto, actualUserResponseDto);

        final ArgumentCaptor<UserRequestDto> captorInitialUserRequestDto = ArgumentCaptor.forClass(UserRequestDto.class);
        verify(service, times(1)).createUser(captorInitialUserRequestDto.capture());
        assertEquals(initialUserRequestDto.name(), captorInitialUserRequestDto.getValue().name());
        assertEquals(initialUserRequestDto.email(), captorInitialUserRequestDto.getValue().email());
    }

    @Test
    void getUserByUuid_shouldReturnUser() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        final var existingUser = new UserResponseDto("John Doe", "john.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var expectedUserResponseDto = new UserResponseDto("John Doe", "john.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        when(service.getUserByUuid(any(UUID.class))).thenReturn(existingUser);

        final UserResponseDto actualUserResponseDto = controller.getUserByUuid(initialUuid);

        assertEquals(expectedUserResponseDto, actualUserResponseDto);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(service, times(1)).getUserByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
    }

    @Test
    void getAllUsers_shouldReturnUserList() {
        final var existingUser1 = new UserResponseDto("John Doe", "john.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var existingUser2 = new UserResponseDto("Jane Doe", "jane.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var expectedUserResponseDto1 = new UserResponseDto("John Doe", "john.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var expectedUserResponseDto2 = new UserResponseDto("Jane Doe", "jane.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final List<UserResponseDto> expectedList = List.of(expectedUserResponseDto1, expectedUserResponseDto2);

        when(service.getAllUsers()).thenReturn(List.of(existingUser1, existingUser2));

        final List<UserResponseDto> actualList = controller.getAllUsers();

        assertIterableEquals(expectedList, actualList);

        verify(service, times(1)).getAllUsers();
    }

    @Test
    void putUserByUuid_shouldUpdateAndReturnUser() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        final var initialUserRequestDto = new UserRequestDto("Jane Doe", "jane.doe@example.com");

        final var updatedUser = new UserResponseDto("Jane Doe", "jane.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var expectedUserResponseDto = new UserResponseDto("Jane Doe", "jane.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        when(service.putUserByUuid(any(UUID.class), any(UserRequestDto.class))).thenReturn(updatedUser);

        final UserResponseDto actualUserResponseDto = controller.putUserByUuid(initialUuid, initialUserRequestDto);

        assertEquals(expectedUserResponseDto, actualUserResponseDto);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        final ArgumentCaptor<UserRequestDto> captorInitialUserRequestDto = ArgumentCaptor.forClass(UserRequestDto.class);
        verify(service, times(1)).putUserByUuid(captorInitialUuid.capture(), captorInitialUserRequestDto.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
        assertEquals(initialUserRequestDto.name(), captorInitialUserRequestDto.getValue().name());
        assertEquals(initialUserRequestDto.email(), captorInitialUserRequestDto.getValue().email());
    }

    @Test
    void patchUserByUuid_shouldPatchAndReturnUser() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        final var initialUserPatchRequestDto = new UserPatchRequestDto("Jane Doe", "jane.doe@example.com");

        final var patchedUser = new UserResponseDto("Jane Doe", "jane.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var expectedUserResponseDto = new UserResponseDto("Jane Doe", "jane.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        when(service.patchUserByUuid(any(UUID.class), any(UserPatchRequestDto.class))).thenReturn(patchedUser);

        final UserResponseDto actualUserResponseDto = controller.patchUserByUuid(initialUuid, initialUserPatchRequestDto);

        assertEquals(expectedUserResponseDto, actualUserResponseDto);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        final ArgumentCaptor<UserPatchRequestDto> captorInitialUserPatchRequestDto = ArgumentCaptor.forClass(UserPatchRequestDto.class);
        verify(service, times(1)).patchUserByUuid(captorInitialUuid.capture(), captorInitialUserPatchRequestDto.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
        assertEquals(initialUserPatchRequestDto.name(), captorInitialUserPatchRequestDto.getValue().name());
        assertEquals(initialUserPatchRequestDto.email(), captorInitialUserPatchRequestDto.getValue().email());
    }

    @Test
    void deleteUserByUuid_shouldCallDeleteOnService() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        controller.deleteUserByUuid(initialUuid);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(service, times(1)).deleteUserByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
    }
}