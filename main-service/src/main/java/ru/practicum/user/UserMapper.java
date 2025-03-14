package ru.practicum.user;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    User toUser(NewUserRequest request);

    UserDto toDto(User user);

    UserShortDto toShortDto(User user);

    List<UserDto> toDto(List<User> users);
}
