package io.github.dfnabiullin.taskmanager.task.service;

import feign.FeignException;
import io.github.dfnabiullin.taskmanager.task.client.UserClient;
import io.github.dfnabiullin.taskmanager.task.dto.TaskPatchRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskResponseDto;
import io.github.dfnabiullin.taskmanager.task.exception.TaskNotFoundException;
import io.github.dfnabiullin.taskmanager.task.exception.UserValidationException;
import io.github.dfnabiullin.taskmanager.task.mapper.TaskMapper;
import io.github.dfnabiullin.taskmanager.task.model.Task;
import io.github.dfnabiullin.taskmanager.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final UserClient userClient;

    @Transactional
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        validateAssignee(taskRequestDto.assigneeUuid());
        Task task = mapper.fromRequestDto(taskRequestDto);
        task.setUuid(UUID.randomUUID());
        return mapper.toResponseDto(repository.save(task));
    }

    @Transactional(readOnly = true)
    public TaskResponseDto getTaskByUuid(UUID uuid) {
        return mapper.toResponseDto(repository.findByUuid(uuid).orElseThrow(() -> new TaskNotFoundException(uuid)));
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAllTasks() {
        return repository.findAll().stream().map(mapper::toResponseDto).toList();
    }

    @Transactional
    public TaskResponseDto putTaskByUuid(UUID uuid, TaskRequestDto taskRequestDto) {
        validateAssignee(taskRequestDto.assigneeUuid());
        Task existingTask = repository.findByUuid(uuid).orElseThrow(() -> new TaskNotFoundException(uuid));
        return mapper.toResponseDto(repository.save(mapper.updateTaskFromDto(taskRequestDto, existingTask)));
    }

    @Transactional
    public TaskResponseDto patchTaskByUuid(UUID uuid, TaskPatchRequestDto taskPatchRequestDto) {
        validateAssignee(taskPatchRequestDto.assigneeUuid());
        Task existingTask = repository.findByUuid(uuid).orElseThrow(() -> new TaskNotFoundException(uuid));
        return mapper.toResponseDto(repository.save(mapper.patchTaskFromDto(taskPatchRequestDto, existingTask)));
    }

    @Transactional
    public void deleteTaskByUuid(UUID uuid) {
        Task existedTask = repository.findByUuid(uuid).orElseThrow(() -> new TaskNotFoundException(uuid));
        repository.delete(existedTask);
    }

    private void validateAssignee(UUID assigneeUuid) {
        if (assigneeUuid == null) {
            return;
        }
        try {
            userClient.checkUserExists(assigneeUuid);
        } catch (FeignException ex) {
            throw new UserValidationException(assigneeUuid);
        }
    }
}