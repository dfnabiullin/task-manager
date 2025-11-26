package io.github.dfnabiullin.taskmanager.task.controller;

import io.github.dfnabiullin.taskmanager.task.dto.TaskPatchRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskResponseDto;
import io.github.dfnabiullin.taskmanager.task.service.TaskService;
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
class TaskControllerTest {
    @Mock
    private TaskService service;
    @InjectMocks
    private TaskController controller;

    @Test
    void createTask_shouldReturnCreatedTask() {
        final var initialTaskRequestDto = createDefaultTaskRequestDto();

        final var createdTask = createDefaultTaskResponseDto();

        final var expectedTaskResponseDto = createDefaultTaskResponseDto();

        when(service.createTask(any(TaskRequestDto.class))).thenReturn(createdTask);

        final TaskResponseDto actualTaskResponseDto = controller.createTask(initialTaskRequestDto);

        assertEquals(expectedTaskResponseDto, actualTaskResponseDto);

        final ArgumentCaptor<TaskRequestDto> captorInitialTaskRequestDto = ArgumentCaptor.forClass(TaskRequestDto.class);
        verify(service, times(1)).createTask(captorInitialTaskRequestDto.capture());
        assertEquals(initialTaskRequestDto.assigneeUuid(), captorInitialTaskRequestDto.getValue().assigneeUuid());
        assertEquals(initialTaskRequestDto.description(), captorInitialTaskRequestDto.getValue().description());
    }

    @Test
    void getTaskByUuid_shouldReturnTask() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

        final var existingTask = createDefaultTaskResponseDto();

        final var expectedTaskResponseDto = createDefaultTaskResponseDto();

        when(service.getTaskByUuid(any(UUID.class))).thenReturn(existingTask);

        final TaskResponseDto actualTaskResponseDto = controller.getTaskByUuid(initialUuid);

        assertEquals(expectedTaskResponseDto, actualTaskResponseDto);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(service, times(1)).getTaskByUuid(captorInitialUuid.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
    }

    @Test
    void getAllTasks_shouldReturnTaskList() {
        final var existingTask1 = createDefaultTaskResponseDto();

        final var existingTask2 = new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"), UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        final var expectedTaskResponseDto1 = createDefaultTaskResponseDto();

        final var expectedTaskResponseDto2 = new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"), UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        final List<TaskResponseDto> expectedList = List.of(expectedTaskResponseDto1, expectedTaskResponseDto2);

        when(service.getAllTasks()).thenReturn(List.of(existingTask1, existingTask2));

        final List<TaskResponseDto> actualList = controller.getAllTasks();

        assertIterableEquals(expectedList, actualList);

        verify(service, times(1)).getAllTasks();
    }

    @Test
    void putTaskByUuid_shouldUpdateAndReturnTask() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

        final var initialTaskRequestDto = new TaskRequestDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        final var updatedTask = new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"), UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        final var expectedTaskResponseDto = new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"), UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        when(service.putTaskByUuid(any(UUID.class), any(TaskRequestDto.class))).thenReturn(updatedTask);

        final TaskResponseDto actualTaskResponseDto = controller.putTaskByUuid(initialUuid, initialTaskRequestDto);

        assertEquals(expectedTaskResponseDto, actualTaskResponseDto);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        final ArgumentCaptor<TaskRequestDto> captorInitialTaskRequestDto = ArgumentCaptor.forClass(TaskRequestDto.class);
        verify(service, times(1)).putTaskByUuid(captorInitialUuid.capture(), captorInitialTaskRequestDto.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
        assertEquals(initialTaskRequestDto.assigneeUuid(), captorInitialTaskRequestDto.getValue().assigneeUuid());
        assertEquals(initialTaskRequestDto.description(), captorInitialTaskRequestDto.getValue().description());
    }

    @Test
    void patchTaskByUuid_shouldPatchAndReturnTask() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

        final var initialTaskPatchRequestDto = new TaskPatchRequestDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        final var patchedTask = new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"), UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        final var expectedTaskResponseDto = new TaskResponseDto(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"), UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.1");

        when(service.patchTaskByUuid(any(UUID.class), any(TaskPatchRequestDto.class))).thenReturn(patchedTask);

        final TaskResponseDto actualTaskResponseDto = controller.patchTaskByUuid(initialUuid, initialTaskPatchRequestDto);

        assertEquals(expectedTaskResponseDto, actualTaskResponseDto);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        final ArgumentCaptor<TaskPatchRequestDto> captorInitialTaskPatchRequestDto = ArgumentCaptor.forClass(TaskPatchRequestDto.class);
        verify(service, times(1)).patchTaskByUuid(captorInitialUuid.capture(), captorInitialTaskPatchRequestDto.capture());
        assertEquals(initialUuid, captorInitialUuid.getValue());
        assertEquals(initialTaskPatchRequestDto.assigneeUuid(), captorInitialTaskPatchRequestDto.getValue()
                .assigneeUuid());
        assertEquals(initialTaskPatchRequestDto.description(), captorInitialTaskPatchRequestDto.getValue()
                .description());
    }

    @Test
    void deleteTaskByUuid_shouldCallDeleteOnService() {
        final UUID initialUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

        controller.deleteTaskByUuid(initialUuid);

        final ArgumentCaptor<UUID> captorInitialUuid = ArgumentCaptor.forClass(UUID.class);
        verify(service, times(1)).deleteTaskByUuid(captorInitialUuid.capture());
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