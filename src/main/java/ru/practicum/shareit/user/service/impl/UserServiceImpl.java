package ru.practicum.shareit.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage storage;
    private final UserMapper um = new UserMapper();

    @Autowired
    public UserServiceImpl(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public Collection<UserDto> getAll() {
        log.info("Обращаемся к хранилищу");
        return um.toUserDtoCollection(storage.getAll());
    }

    @Override
    public UserDto getById(long id) {
        log.info("Обращаемся к хранилищу");
        User user = storage.getById(id).orElseThrow(() -> {
            log.warn("Сущность с id {} не найдена", id);
            return new NotFoundException(String.format("Сущность с id %d не найдена", id));
        });
        return um.toUserDto(user);
    }

    @Override
    public boolean delete(long id) {
        log.info("Проверяем наличие сущности с id {}", id);
        getById(id);
        log.info("Сущность с id {} найдена, обращаемся к хранилищу для удаления", id);
        return storage.delete(id);
    }

    @Override
    public UserDto create(UserDto element) {
        log.info("Обращаемся к хранилищу");
        User user = storage.create(um.toUser(element));
        return um.toUserDto(user);
    }

    @Override
    public UserDto update(UserDto element) {
        log.info("Проверяем наличие пользователя с id {}", element.getId());
        User userForUpdate = um.toUser(getById(element.getId()));
        log.info("Пользователь найден");
        if (element.getName() != null) {
            log.info("Обновляем имя на {}", element.getName());
            userForUpdate.setName(element.getName());
        }
        if (element.getEmail() != null) {
            log.info("Обновляем email на {}", element.getEmail());
            userForUpdate.setEmail(element.getEmail());
        }
        log.info("Обращаемся к хранилищу");
        return um.toUserDto(storage.update(userForUpdate));
    }
}
