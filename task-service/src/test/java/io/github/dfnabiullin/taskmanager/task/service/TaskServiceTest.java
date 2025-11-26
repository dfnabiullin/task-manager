package io.github.dfnabiullin.taskmanager.task.service;

import io.github.dfnabiullin.taskmanager.task.dto.TaskPatchRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskResponseDto;
import io.github.dfnabiullin.taskmanager.task.exception.TaskNotFoundException;
import io.github.dfnabiullin.taskmanager.task.mapper.TaskMapper;
import io.github.dfnabiullin.taskmanager.task.model.Task;
import io.github.dfnabiullin.taskmanager.task.repository.TaskRepository;
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
class TaskServiceTest {
    @Mock
    private TaskRepository repository;
    @Mock
    private TaskMapper mapper;
    @InjectMocks
    private TaskService service;

    @Test
    void createTask_shouldReturnSavedTask() {
        final var initialTaskRequestDto = createDefaultTaskRequestDto();

        final var mappedTask = new Task();
        mappedTask.setAssigneeUuid(createDefaultUuid());
        mappedTask.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

        final var savedTask = new Task();
        savedTask.setAssigneeUuid(createDefaultUuid());
        savedTask.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        savedTask.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var expectedTaskResponseDto = new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), createDefaultUuid(), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

        when(mapper.fromRequestDto(any(TaskRequestDto.class))).thenReturn(mappedTask);
        when(repository.save(any(Task.class))).thenReturn(savedTask);
        when(mapper.toResponseDto(any(Task.class))).thenReturn(expectedTaskResponseDto);

        final TaskResponseDto actualTaskResponseDto = service.createTask(initialTaskRequestDto);

        assertEquals(expectedTaskResponseDto, actualTaskResponseDto);

        final ArgumentCaptor<TaskRequestDto> captorInitialTask = ArgumentCaptor.forClass(TaskRequestDto.class);
        verify(mapper, times(1)).fromRequestDto(captorInitialTask.capture());
        assertEquals(initialTaskRequestDto.assigneeUuid(), captorInitialTask.getValue().assigneeUuid());
        assertEquals(initialTaskRequestDto.description(), captorInitialTask.getValue().description());

        final ArgumentCaptor<Task> captorMappedTask = ArgumentCaptor.forClass(Task.class);
        verify(repository, times(1)).save(captorMappedTask.capture());
        assertEquals(mappedTask.getAssigneeUuid(), captorMappedTask.getValue().getAssigneeUuid());
        assertEquals(mappedTask.getDescription(), captorMappedTask.getValue().getDescription());
        assertNotNull(captorMappedTask.getValue().getUuid());

        final ArgumentCaptor<Task> captorSavedTask = ArgumentCaptor.forClass(Task.class);
        verify(mapper, times(1)).toResponseDto(captorSavedTask.capture());
        assertEquals(savedTask.getAssigneeUuid(), captorSavedTask.getValue().getAssigneeUuid());
        assertEquals(savedTask.getDescription(), captorSavedTask.getValue().getDescription());
        assertEquals(savedTask.getUuid(), captorSavedTask.getValue().getUuid());
    }

    @Test
    void getTaskByUuid_whenTaskExists_shouldReturnTask() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

        final var existingTask = new Task();
        existingTask.setAssigneeUuid(createDefaultUuid());
        existingTask.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        existingTask.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var expectedTaskResponseDto = new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), createDefaultUuid(), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.of(existingTask));
        when(mapper.toResponseDto(any(Task.class))).thenReturn(expectedTaskResponseDto);

        final TaskResponseDto actualTaskResponseDto = service.getTaskByUuid(initialUuid);

        assertEquals(expectedTaskResponseDto, actualTaskResponseDto);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());

        final ArgumentCaptor<Task> captorExistingTask = ArgumentCaptor.forClass(Task.class);
        verify(mapper, times(1)).toResponseDto(captorExistingTask.capture());
        assertEquals(existingTask.getAssigneeUuid(), captorExistingTask.getValue().getAssigneeUuid());
        assertEquals(existingTask.getDescription(), captorExistingTask.getValue().getDescription());
        assertEquals(existingTask.getUuid(), captorExistingTask.getValue().getUuid());
    }

    @Test
    void getTaskByUuid_whenTaskDoesNotExist_shouldThrowTaskNotFoundException() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> service.getTaskByUuid(initialUuid));

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
    }

    @Test
    void getAllTasks_whenTasksExist_shouldReturnTaskList() {
        final var existingTask1 = new Task();
        existingTask1.setAssigneeUuid(createDefaultUuid());
        existingTask1.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        existingTask1.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var existingTask2 = new Task();
        existingTask2.setAssigneeUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"));
        existingTask2.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");
        existingTask2.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"));

        final var expectedTaskResponseDto1 = new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), createDefaultUuid(), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

        final var expectedTaskResponseDto2 = new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"), UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        final List<TaskResponseDto> expectedList = List.of(expectedTaskResponseDto1, expectedTaskResponseDto2);

        when(repository.findAll()).thenReturn(List.of(existingTask1, existingTask2));
        when(mapper.toResponseDto(any(Task.class))).thenReturn(expectedTaskResponseDto1, expectedTaskResponseDto2);

        final List<TaskResponseDto> actualList = service.getAllTasks();

        assertIterableEquals(expectedList, actualList);

        verify(repository, times(1)).findAll();

        final ArgumentCaptor<Task> captorExistingTask = ArgumentCaptor.forClass(Task.class);
        verify(mapper, times(2)).toResponseDto(captorExistingTask.capture());
        final List<Task> captorList = captorExistingTask.getAllValues();
        assertEquals(existingTask1.getAssigneeUuid(), captorList.getFirst().getAssigneeUuid());
        assertEquals(existingTask1.getDescription(), captorList.getFirst().getDescription());
        assertEquals(existingTask1.getUuid(), captorList.getFirst().getUuid());
        assertEquals(existingTask2.getAssigneeUuid(), captorList.get(1).getAssigneeUuid());
        assertEquals(existingTask2.getDescription(), captorList.get(1).getDescription());
        assertEquals(existingTask2.getUuid(), captorList.get(1).getUuid());
    }

    @Test
    void getAllTasks_whenNoTasksExist_shouldReturnEmptyList() {
        final List<TaskResponseDto> expectedList = List.of();

        when(repository.findAll()).thenReturn(List.of());

        final List<TaskResponseDto> actualList = service.getAllTasks();

        assertIterableEquals(expectedList, actualList);

        verify(repository, times(1)).findAll();

        verify(mapper, never()).toResponseDto(any());
    }

    @Test
    void putTaskByUuid_whenTaskExists_shouldUpdateAndReturnTask() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        final var initialTaskRequestDto = new TaskRequestDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        final var mappedTask = new Task();
        mappedTask.setAssigneeUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"));
        mappedTask.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");
        mappedTask.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var existingTask = new Task();
        existingTask.setAssigneeUuid(createDefaultUuid());
        existingTask.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        existingTask.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var updatedTask = new Task();
        updatedTask.setAssigneeUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"));
        updatedTask.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");
        updatedTask.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var savedTask = new Task();
        savedTask.setAssigneeUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"));
        savedTask.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");
        savedTask.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var expectedTaskResponseDto = new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"), UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.of(existingTask));
        when(mapper.updateTaskFromDto(any(TaskRequestDto.class), any(Task.class))).thenReturn(mappedTask);
        when(repository.save(any(Task.class))).thenReturn(savedTask);
        when(mapper.toResponseDto(any(Task.class))).thenReturn(expectedTaskResponseDto);

        final TaskResponseDto actualTaskResponseDto = service.putTaskByUuid(initialUuid, initialTaskRequestDto);

        assertEquals(expectedTaskResponseDto, actualTaskResponseDto);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());

        final ArgumentCaptor<TaskRequestDto> captorInitialTask = ArgumentCaptor.forClass(TaskRequestDto.class);
        final ArgumentCaptor<Task> captorExistingTask1 = ArgumentCaptor.forClass(Task.class);
        verify(mapper, times(1)).updateTaskFromDto(captorInitialTask.capture(), captorExistingTask1.capture());
        assertEquals(initialTaskRequestDto.assigneeUuid(), captorInitialTask.getValue().assigneeUuid());
        assertEquals(initialTaskRequestDto.description(), captorInitialTask.getValue().description());
        assertEquals(existingTask.getAssigneeUuid(), captorExistingTask1.getValue().getAssigneeUuid());
        assertEquals(existingTask.getDescription(), captorExistingTask1.getValue().getDescription());
        assertEquals(existingTask.getUuid(), captorExistingTask1.getValue().getUuid());

        final ArgumentCaptor<Task> captorUpdatedTask = ArgumentCaptor.forClass(Task.class);
        verify(repository, times(1)).save(captorUpdatedTask.capture());
        assertEquals(updatedTask.getAssigneeUuid(), captorUpdatedTask.getValue().getAssigneeUuid());
        assertEquals(updatedTask.getDescription(), captorUpdatedTask.getValue().getDescription());
        assertEquals(updatedTask.getUuid(), captorUpdatedTask.getValue().getUuid());

        final ArgumentCaptor<Task> captorSavedTask = ArgumentCaptor.forClass(Task.class);
        verify(mapper, times(1)).toResponseDto(captorSavedTask.capture());
        assertEquals(savedTask.getAssigneeUuid(), captorSavedTask.getValue().getAssigneeUuid());
        assertEquals(savedTask.getDescription(), captorSavedTask.getValue().getDescription());
        assertEquals(savedTask.getUuid(), captorSavedTask.getValue().getUuid());
    }

    @Test
    void putTaskByUuid_whenTaskDoesNotExist_shouldThrowTaskNotFoundException() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        final var initialTaskRequestDto = new TaskRequestDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> service.putTaskByUuid(initialUuid, initialTaskRequestDto));

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
    }

    @Test
    void patchTaskByUuid_whenTaskExist_shouldPatchAndReturnTask() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        final var initialTaskPatchRequestDto = new TaskPatchRequestDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        final var mappedTask = new Task();
        mappedTask.setAssigneeUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"));
        mappedTask.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");
        mappedTask.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var existingTask = new Task();
        existingTask.setAssigneeUuid(createDefaultUuid());
        existingTask.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        existingTask.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var patchedTask = new Task();
        patchedTask.setAssigneeUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"));
        patchedTask.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");
        patchedTask.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var savedTask = new Task();
        savedTask.setAssigneeUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"));
        savedTask.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");
        savedTask.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        final var expectedTaskResponseDto = new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"), UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.of(existingTask));
        when(mapper.patchTaskFromDto(any(TaskPatchRequestDto.class), any(Task.class))).thenReturn(mappedTask);
        when(repository.save(any(Task.class))).thenReturn(savedTask);
        when(mapper.toResponseDto(any(Task.class))).thenReturn(expectedTaskResponseDto);

        final TaskResponseDto actualTaskResponseDto = service.patchTaskByUuid(initialUuid, initialTaskPatchRequestDto);

        assertEquals(expectedTaskResponseDto, actualTaskResponseDto);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());

        final ArgumentCaptor<TaskPatchRequestDto> captorInitialTask = ArgumentCaptor.forClass(TaskPatchRequestDto.class);
        final ArgumentCaptor<Task> captorExistingTask1 = ArgumentCaptor.forClass(Task.class);
        verify(mapper, times(1)).patchTaskFromDto(captorInitialTask.capture(), captorExistingTask1.capture());
        assertEquals(initialTaskPatchRequestDto.assigneeUuid(), captorInitialTask.getValue().assigneeUuid());
        assertEquals(initialTaskPatchRequestDto.description(), captorInitialTask.getValue().description());
        assertEquals(existingTask.getAssigneeUuid(), captorExistingTask1.getValue().getAssigneeUuid());
        assertEquals(existingTask.getDescription(), captorExistingTask1.getValue().getDescription());
        assertEquals(existingTask.getUuid(), captorExistingTask1.getValue().getUuid());

        final ArgumentCaptor<Task> captorPatchedTask = ArgumentCaptor.forClass(Task.class);
        verify(repository, times(1)).save(captorPatchedTask.capture());
        assertEquals(patchedTask.getAssigneeUuid(), captorPatchedTask.getValue().getAssigneeUuid());
        assertEquals(patchedTask.getDescription(), captorPatchedTask.getValue().getDescription());
        assertEquals(patchedTask.getUuid(), captorPatchedTask.getValue().getUuid());

        final ArgumentCaptor<Task> captorSavedTask = ArgumentCaptor.forClass(Task.class);
        verify(mapper, times(1)).toResponseDto(captorSavedTask.capture());
        assertEquals(savedTask.getAssigneeUuid(), captorSavedTask.getValue().getAssigneeUuid());
        assertEquals(savedTask.getDescription(), captorSavedTask.getValue().getDescription());
        assertEquals(savedTask.getUuid(), captorSavedTask.getValue().getUuid());
    }

    @Test
    void patchTaskByUuid_whenTaskDoesNotExist_shouldThrowTaskNotFoundException() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        final var initialTaskPatchRequestDto = new TaskPatchRequestDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> service.patchTaskByUuid(initialUuid, initialTaskPatchRequestDto));

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
    }

    @Test
    void deleteTaskByUuid_whenTaskExists_shouldCallDeleteOnRepository() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

        final var existingTask = new Task();
        existingTask.setAssigneeUuid(createDefaultUuid());
        existingTask.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        existingTask.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.of(existingTask));

        service.deleteTaskByUuid(initialUuid);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());

        verify(repository, times(1)).delete(existingTask);
    }

    @Test
    void deleteTaskByUuid_whenTaskDoesNotExist_shouldThrowTaskNotFoundException() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

        when(repository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> service.deleteTaskByUuid(initialUuid));

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(repository, times(1)).findByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
    }

    private UUID createDefaultUuid() {
        return UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    }

    private TaskRequestDto createDefaultTaskRequestDto() {
        return new TaskRequestDto(createDefaultUuid(), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    }

    private TaskResponseDto createDefaultTaskResponseDto() {
        return new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), createDefaultUuid(), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    }
}