package io.github.dfnabiullin.taskmanager.task.controller;

import io.github.dfnabiullin.taskmanager.task.dto.TaskPatchRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskResponseDto;
import io.github.dfnabiullin.taskmanager.task.service.TaskService;
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
@RequestMapping("/api/v1/tasks")
@Tag(name = "Tasks", description = "Methods for working with tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;

    @Operation(summary = "Creating a new task")
    @ApiResponse(responseCode = "201", description = "The task was successfully created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto createTask(@RequestBody @Valid TaskRequestDto taskRequestDto) {
        return service.createTask(taskRequestDto);
    }

    @Operation(summary = "Getting a task by UUID")
    @ApiResponse(responseCode = "200", description = "The task was found")
    @GetMapping("/{uuid}")
    public TaskResponseDto getTaskByUuid(@PathVariable UUID uuid) {
        return service.getTaskByUuid(uuid);
    }

    @Operation(summary = "Getting a list of all tasks")
    @ApiResponse(responseCode = "200", description = "The task was found")
    @GetMapping
    public List<TaskResponseDto> getAllTasks() {
        return service.getAllTasks();
    }

    @Operation(summary = "Full task update")
    @ApiResponse(responseCode = "200", description = "The task has been successfully updated")
    @PutMapping("/{uuid}")
    public TaskResponseDto putTaskByUuid(@PathVariable UUID uuid, @RequestBody @Valid TaskRequestDto taskRequestDto) {
        return service.putTaskByUuid(uuid, taskRequestDto);
    }

    @Operation(summary = "Partial task update")
    @ApiResponse(responseCode = "200", description = "The task has been partially updated successfully")
    @PatchMapping("/{uuid}")
    public TaskResponseDto patchTaskByUuid(@PathVariable UUID uuid, @RequestBody @Valid TaskPatchRequestDto taskPatchRequestDto) {
        return service.patchTaskByUuid(uuid, taskPatchRequestDto);
    }

    @Operation(summary = "Deleting a task")
    @ApiResponse(responseCode = "204", description = "The task has been deleted")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskByUuid(@PathVariable UUID uuid) {
        service.deleteTaskByUuid(uuid);
    }
}