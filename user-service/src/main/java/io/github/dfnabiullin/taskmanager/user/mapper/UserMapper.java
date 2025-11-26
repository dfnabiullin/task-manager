package io.github.dfnabiullin.taskmanager.user.mapper;

import io.github.dfnabiullin.taskmanager.user.dto.UserPatchRequestDto;
import io.github.dfnabiullin.taskmanager.user.dto.UserRequestDto;
import io.github.dfnabiullin.taskmanager.user.dto.UserResponseDto;
import io.github.dfnabiullin.taskmanager.user.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toResponseDto(User user);

    @ToEntityMapping
    User fromRequestDto(UserRequestDto userRequestDto);

    @ToEntityMapping
    User updateUserFromDto(UserRequestDto dto, @MappingTarget User user);

    @ToEntityMapping
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User patchUserFromDto(UserPatchRequestDto dto, @MappingTarget User user);
}