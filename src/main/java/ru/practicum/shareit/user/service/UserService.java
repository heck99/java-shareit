package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAll();

    UserDto getById(long id);

    boolean delete(long id);

    UserDto create(UserDto element);

    UserDto update(UserDto element);
}
