package io.github.dfnabiullin.taskmanager.task.service;

import io.github.dfnabiullin.taskmanager.task.dto.TaskPatchRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskResponseDto;
import io.github.dfnabiullin.taskmanager.task.exception.TaskNotFoundException;
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

    @Transactional
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
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
        Task existingTask = repository.findByUuid(uuid).orElseThrow(() -> new TaskNotFoundException(uuid));
        return mapper.toResponseDto(repository.save(mapper.updateTaskFromDto(taskRequestDto, existingTask)));
    }

    @Transactional
    public TaskResponseDto patchTaskByUuid(UUID uuid, TaskPatchRequestDto taskPatchRequestDto) {
        Task existingTask = repository.findByUuid(uuid).orElseThrow(() -> new TaskNotFoundException(uuid));
        return mapper.toResponseDto(repository.save(mapper.patchTaskFromDto(taskPatchRequestDto, existingTask)));
    }

    @Transactional
    public void deleteTaskByUuid(UUID uuid) {
        Task existedTask = repository.findByUuid(uuid).orElseThrow(() -> new TaskNotFoundException(uuid));
        repository.delete(existedTask);
    }
}