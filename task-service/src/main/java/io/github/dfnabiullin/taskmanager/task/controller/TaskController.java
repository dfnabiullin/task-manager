package io.github.dfnabiullin.taskmanager.task.controller;

import io.github.dfnabiullin.taskmanager.task.dto.TaskPatchRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskResponseDto;
import io.github.dfnabiullin.taskmanager.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto createTask(@RequestBody @Valid TaskRequestDto taskRequestDto) {
        return service.createTask(taskRequestDto);
    }

    @GetMapping("/{uuid}")
    public TaskResponseDto getTaskByUuid(@PathVariable UUID uuid) {
        return service.getTaskByUuid(uuid);
    }

    @GetMapping
    public List<TaskResponseDto> getAllTasks() {
        return service.getAllTasks();
    }

    @PutMapping("/{uuid}")
    public TaskResponseDto putTaskByUuid(@PathVariable UUID uuid, @RequestBody @Valid TaskRequestDto taskRequestDto) {
        return service.putTaskByUuid(uuid, taskRequestDto);
    }

    @PatchMapping("/{uuid}")
    public TaskResponseDto patchTaskByUuid(@PathVariable UUID uuid, @RequestBody @Valid TaskPatchRequestDto taskPatchRequestDto) {
        return service.patchTaskByUuid(uuid, taskPatchRequestDto);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskByUuid(@PathVariable UUID uuid) {
        service.deleteTaskByUuid(uuid);
    }
}