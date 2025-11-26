package io.github.dfnabiullin.taskmanager.user.service;

import io.github.dfnabiullin.taskmanager.user.dto.UserPatchRequestDto;
import io.github.dfnabiullin.taskmanager.user.dto.UserRequestDto;
import io.github.dfnabiullin.taskmanager.user.dto.UserResponseDto;
import io.github.dfnabiullin.taskmanager.user.exception.UserNotFoundException;
import io.github.dfnabiullin.taskmanager.user.mapper.UserMapper;
import io.github.dfnabiullin.taskmanager.user.model.User;
import io.github.dfnabiullin.taskmanager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = mapper.fromRequestDto(userRequestDto);
        user.setUuid(UUID.randomUUID());
        return mapper.toResponseDto(repository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByUuid(UUID uuid) {
        return mapper.toResponseDto(repository.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid)));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return repository.findAll().stream().map(mapper::toResponseDto).toList();
    }

    @Transactional
    public UserResponseDto putUserByUuid(UUID uuid, UserRequestDto userRequestDto) {
        User existingUser = repository.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
        return mapper.toResponseDto(repository.save(mapper.updateUserFromDto(userRequestDto, existingUser)));
    }

    @Transactional
    public UserResponseDto patchUserByUuid(UUID uuid, UserPatchRequestDto userPatchRequestDto) {
        User existingUser = repository.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
        return mapper.toResponseDto(repository.save(mapper.patchUserFromDto(userPatchRequestDto, existingUser)));
    }

    @Transactional
    public void deleteUserByUuid(UUID uuid) {
        User existedUser = repository.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
        repository.delete(existedUser);
    }
}