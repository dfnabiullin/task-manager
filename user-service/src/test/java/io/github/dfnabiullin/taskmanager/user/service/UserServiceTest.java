package io.github.dfnabiullin.taskmanager.user.service;

import io.github.dfnabiullin.taskmanager.user.dto.UserPatchRequestDto;
import io.github.dfnabiullin.taskmanager.user.dto.UserRequestDto;
import io.github.dfnabiullin.taskmanager.user.dto.UserResponseDto;
import io.github.dfnabiullin.taskmanager.user.exception.UserNotFoundException;
import io.github.dfnabiullin.taskmanager.user.mapper.UserMapper;
import io.github.dfnabiullin.taskmanager.user.model.User;
import io.github.dfnabiullin.taskmanager.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @InjectMocks
    private UserService service;

    @Test
    void createUser_shouldReturnSavedUser() {
        final var initialUserRequestDto = new UserRequestDto("John Doe", "john.doe@example.com");

        final var mappedUser = new User();
        mappedUser.setName("John Doe");
        mappedUser.setEmail("john.doe@example.com");

        final var savedUser = new User();
        savedUser.setName("John Doe");
        savedUser.setEmail("john.doe@example.com");
        savedUser.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var expectedUserResponseDto = new UserResponseDto("John Doe", "john.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        when(mapper.fromRequestDto(any(UserRequestDto.class))).thenReturn(mappedUser);
        when(repository.save(any(User.class))).thenReturn(savedUser);
        when(mapper.toResponseDto(any(User.class))).thenReturn(expectedUserResponseDto);

        final UserResponseDto actualUserResponseDto = service.createUser(initialUserRequestDto);

        assertEquals(expectedUserResponseDto, actualUserResponseDto);

        final ArgumentCaptor<UserRequestDto> captorInitialUser = ArgumentCaptor.forClass(UserRequestDto.class);
        verify(mapper, times(1)).fromRequestDto(captorInitialUser.capture());
        assertEquals(initialUserRequestDto.name(), captorInitialUser.getValue().name());
        assertEquals(initialUserRequestDto.email(), captorInitialUser.getValue().email());

        final ArgumentCaptor<User> captorMappedUser = ArgumentCaptor.forClass(User.class);
        verify(repository, times(1)).save(captorMappedUser.capture());
        assertEquals(mappedUser.getName(), captorMappedUser.getValue().getName());
        assertEquals(mappedUser.getEmail(), captorMappedUser.getValue().getEmail());
        assertNotNull(captorMappedUser.getValue().getUuid());

        final ArgumentCaptor<User> captorSavedUser = ArgumentCaptor.forClass(User.class);
        verify(mapper, times(1)).toResponseDto(captorSavedUser.capture());
        assertEquals(savedUser.getName(), captorSavedUser.getValue().getName());
        assertEquals(savedUser.getEmail(), captorSavedUser.getValue().getEmail());
        assertEquals(savedUser.getUuid(), captorSavedUser.getValue().getUuid());
    }

    @Test
    void getUserByUuid_whenUserExists_shouldReturnUser() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        final var existingUser = new User();
        existingUser.setName("John Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var expectedUserResponseDto = new UserResponseDto("John Doe", "john.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.of(existingUser));
        when(mapper.toResponseDto(any(User.class))).thenReturn(expectedUserResponseDto);

        final UserResponseDto actualUserResponseDto = service.getUserByUuid(initialUuid);

        assertEquals(expectedUserResponseDto, actualUserResponseDto);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());

        final ArgumentCaptor<User> captorExistingUser = ArgumentCaptor.forClass(User.class);
        verify(mapper, times(1)).toResponseDto(captorExistingUser.capture());
        assertEquals(existingUser.getName(), captorExistingUser.getValue().getName());
        assertEquals(existingUser.getEmail(), captorExistingUser.getValue().getEmail());
        assertEquals(existingUser.getUuid(), captorExistingUser.getValue().getUuid());
    }

    @Test
    void getUserByUuid_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getUserByUuid(initialUuid));

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
    }

    @Test
    void getAllUsers_whenUsersExist_shouldReturnUserList() {
        final var existingUser1 = new User();
        existingUser1.setName("John Doe");
        existingUser1.setEmail("john.doe@example.com");
        existingUser1.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var existingUser2 = new User();
        existingUser2.setName("Jane Doe");
        existingUser2.setEmail("jane.doe@example.com");
        existingUser2.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var expectedUserResponseDto1 = new UserResponseDto("John Doe", "john.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var expectedUserResponseDto2 = new UserResponseDto("Jane Doe", "jane.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final List<UserResponseDto> expectedList = List.of(expectedUserResponseDto1, expectedUserResponseDto2);

        when(repository.findAll()).thenReturn(List.of(existingUser1, existingUser2));
        when(mapper.toResponseDto(any(User.class))).thenReturn(expectedUserResponseDto1, expectedUserResponseDto2);

        final List<UserResponseDto> actualList = service.getAllUsers();

        assertIterableEquals(expectedList, actualList);

        verify(repository, times(1)).findAll();

        final ArgumentCaptor<User> captorExistingUser = ArgumentCaptor.forClass(User.class);
        verify(mapper, times(2)).toResponseDto(captorExistingUser.capture());
        final List<User> captorList = captorExistingUser.getAllValues();
        assertEquals(existingUser1.getName(), captorList.getFirst().getName());
        assertEquals(existingUser1.getEmail(), captorList.getFirst().getEmail());
        assertEquals(existingUser1.getUuid(), captorList.getFirst().getUuid());
        assertEquals(existingUser2.getName(), captorList.get(1).getName());
        assertEquals(existingUser2.getEmail(), captorList.get(1).getEmail());
        assertEquals(existingUser2.getUuid(), captorList.get(1).getUuid());
    }

    @Test
    void getAllUsers_whenNoUsersExist_shouldReturnEmptyList() {
        final List<UserResponseDto> expectedList = List.of();

        when(repository.findAll()).thenReturn(List.of());

        final List<UserResponseDto> actualList = service.getAllUsers();

        assertIterableEquals(expectedList, actualList);

        verify(repository, times(1)).findAll();

        verify(mapper, never()).toResponseDto(any());
    }

    @Test
    void putUserByUuid_whenUserExists_shouldUpdateAndReturnUser() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        final var initialUserRequestDto = new UserRequestDto("Jane Doe", "jane.doe@example.com");

        final var mappedUser = new User();
        mappedUser.setName("Jane Doe");
        mappedUser.setEmail("jane.doe@example.com");
        mappedUser.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var existingUser = new User();
        existingUser.setName("John Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var updatedUser = new User();
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane.doe@example.com");
        updatedUser.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var savedUser = new User();
        savedUser.setName("Jane Doe");
        savedUser.setEmail("jane.doe@example.com");
        savedUser.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var expectedUserResponseDto = new UserResponseDto("Jane Doe", "jane.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.of(existingUser));
        when(mapper.updateUserFromDto(any(UserRequestDto.class), any(User.class))).thenReturn(mappedUser);
        when(repository.save(any(User.class))).thenReturn(savedUser);
        when(mapper.toResponseDto(any(User.class))).thenReturn(expectedUserResponseDto);

        final UserResponseDto actualUserResponseDto = service.putUserByUuid(initialUuid, initialUserRequestDto);

        assertEquals(expectedUserResponseDto, actualUserResponseDto);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());

        final ArgumentCaptor<UserRequestDto> captorInitialUser = ArgumentCaptor.forClass(UserRequestDto.class);
        final ArgumentCaptor<User> captorExistingUser1 = ArgumentCaptor.forClass(User.class);
        verify(mapper, times(1)).updateUserFromDto(captorInitialUser.capture(), captorExistingUser1.capture());
        assertEquals(initialUserRequestDto.name(), captorInitialUser.getValue().name());
        assertEquals(initialUserRequestDto.email(), captorInitialUser.getValue().email());
        assertEquals(existingUser.getName(), captorExistingUser1.getValue().getName());
        assertEquals(existingUser.getEmail(), captorExistingUser1.getValue().getEmail());
        assertEquals(existingUser.getUuid(), captorExistingUser1.getValue().getUuid());

        final ArgumentCaptor<User> captorUpdatedUser = ArgumentCaptor.forClass(User.class);
        verify(repository, times(1)).save(captorUpdatedUser.capture());
        assertEquals(updatedUser.getName(), captorUpdatedUser.getValue().getName());
        assertEquals(updatedUser.getEmail(), captorUpdatedUser.getValue().getEmail());
        assertEquals(updatedUser.getUuid(), captorUpdatedUser.getValue().getUuid());

        final ArgumentCaptor<User> captorSavedUser = ArgumentCaptor.forClass(User.class);
        verify(mapper, times(1)).toResponseDto(captorSavedUser.capture());
        assertEquals(savedUser.getName(), captorSavedUser.getValue().getName());
        assertEquals(savedUser.getEmail(), captorSavedUser.getValue().getEmail());
        assertEquals(savedUser.getUuid(), captorSavedUser.getValue().getUuid());
    }

    @Test
    void putUserByUuid_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        final var initialUserRequestDto = new UserRequestDto("Jane Doe", "jane.doe@example.com");

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.putUserByUuid(initialUuid, initialUserRequestDto));

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
    }

    @Test
    void patchUserByUuid_whenUserExist_shouldPatchAndReturnUser() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        final var initialUserPatchRequestDto = new UserPatchRequestDto("Jane Doe", "jane.doe@example.com");

        final var mappedUser = new User();
        mappedUser.setName("Jane Doe");
        mappedUser.setEmail("jane.doe@example.com");
        mappedUser.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var existingUser = new User();
        existingUser.setName("John Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var patchedUser = new User();
        patchedUser.setName("Jane Doe");
        patchedUser.setEmail("jane.doe@example.com");
        patchedUser.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var savedUser = new User();
        savedUser.setName("Jane Doe");
        savedUser.setEmail("jane.doe@example.com");
        savedUser.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        final var expectedUserResponseDto = new UserResponseDto("Jane Doe", "jane.doe@example.com", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.of(existingUser));
        when(mapper.patchUserFromDto(any(UserPatchRequestDto.class), any(User.class))).thenReturn(mappedUser);
        when(repository.save(any(User.class))).thenReturn(savedUser);
        when(mapper.toResponseDto(any(User.class))).thenReturn(expectedUserResponseDto);

        final UserResponseDto actualUserResponseDto = service.patchUserByUuid(initialUuid, initialUserPatchRequestDto);

        assertEquals(expectedUserResponseDto, actualUserResponseDto);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());

        final ArgumentCaptor<UserPatchRequestDto> captorInitialUser = ArgumentCaptor.forClass(UserPatchRequestDto.class);
        final ArgumentCaptor<User> captorExistingUser1 = ArgumentCaptor.forClass(User.class);
        verify(mapper, times(1)).patchUserFromDto(captorInitialUser.capture(), captorExistingUser1.capture());
        assertEquals(initialUserPatchRequestDto.name(), captorInitialUser.getValue().name());
        assertEquals(initialUserPatchRequestDto.email(), captorInitialUser.getValue().email());
        assertEquals(existingUser.getName(), captorExistingUser1.getValue().getName());
        assertEquals(existingUser.getEmail(), captorExistingUser1.getValue().getEmail());
        assertEquals(existingUser.getUuid(), captorExistingUser1.getValue().getUuid());

        final ArgumentCaptor<User> captorPatchedUser = ArgumentCaptor.forClass(User.class);
        verify(repository, times(1)).save(captorPatchedUser.capture());
        assertEquals(patchedUser.getName(), captorPatchedUser.getValue().getName());
        assertEquals(patchedUser.getEmail(), captorPatchedUser.getValue().getEmail());
        assertEquals(patchedUser.getUuid(), captorPatchedUser.getValue().getUuid());

        final ArgumentCaptor<User> captorSavedUser = ArgumentCaptor.forClass(User.class);
        verify(mapper, times(1)).toResponseDto(captorSavedUser.capture());
        assertEquals(savedUser.getName(), captorSavedUser.getValue().getName());
        assertEquals(savedUser.getEmail(), captorSavedUser.getValue().getEmail());
        assertEquals(savedUser.getUuid(), captorSavedUser.getValue().getUuid());
    }

    @Test
    void patchUserByUuid_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        final var initialUserPatchRequestDto = new UserPatchRequestDto("Jane Doe", "jane.doe@example.com");

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.patchUserByUuid(initialUuid, initialUserPatchRequestDto));

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
    }

    @Test
    void deleteUserByUuid_whenUserExists_shouldCallDeleteOnRepository() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        final var existingUser = new User();
        existingUser.setName("John Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.of(existingUser));

        service.deleteUserByUuid(initialUuid);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());

        verify(repository, times(1)).delete(existingUser);
    }

    @Test
    void deleteUserByUuid_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.deleteUserByUuid(initialUuid));

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
    }
}