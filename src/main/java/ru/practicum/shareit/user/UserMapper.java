package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserMapper {
    public User toUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .id(userDto.getId())
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }

    public Collection<UserDto> toUserDtoCollection(Collection<User> users) {
        return users.stream().map(this::toUserDto).collect(Collectors.toList());
    }
}
