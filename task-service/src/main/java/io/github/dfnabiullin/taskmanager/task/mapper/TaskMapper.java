package io.github.dfnabiullin.taskmanager.task.mapper;

import io.github.dfnabiullin.taskmanager.task.dto.TaskPatchRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskRequestDto;
import io.github.dfnabiullin.taskmanager.task.dto.TaskResponseDto;
import io.github.dfnabiullin.taskmanager.task.model.Task;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskResponseDto toResponseDto(Task task);

    @ToEntityMapping
    Task fromRequestDto(TaskRequestDto taskRequestDto);

    @ToEntityMapping
    Task updateTaskFromDto(TaskRequestDto taskRequestDto, @MappingTarget Task task);

    @ToEntityMapping
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task patchTaskFromDto(TaskPatchRequestDto taskPatchRequestDto, @MappingTarget Task task);
}