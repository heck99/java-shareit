package ru.practicum.shareit.item.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotAuthentication;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Collections;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;
    private final ItemMapper im = new ItemMapper();
    private final UserMapper um = new UserMapper();

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    @Override
    public Collection<ItemDto> getAllByUserId(long id) {
        log.info("Проверяем наличие пользователя с id {}", id);
        userService.getById(id);
        log.info("Пользователь найден обращаемся к хранилищу");
        return im.toItemDtoCollection(itemStorage.getAllByUserId(id));
    }

    @Override
    public ItemDto getById(long id) {
        log.info("Обращаемся к хранилищу");
        Item item = itemStorage.getById(id).orElseThrow(
                () -> {
                    log.warn("Вещь с id {} не найдена", id);
                    return new NotFoundException(String.format("Сущность с id %d не найдена", id));
                });
        return im.toItemDto(item);
    }

    @Override
    public boolean delete(long id, long userId) {
        log.info("Проверяем наличие пользователя с id {}", userId);
        userService.getById(userId);
        log.info("Пользователь найден, проверяем наличие вещи с id {}", id);
        Item item = im.toItem(getById(id));
        log.info("Вещь найдена, проверяем, что вещь с id {} принадлежит пользователю с id {}", id, userId);
        checkAuth(userId, item);
        log.info("Обращаемся к хранилищу");
        return itemStorage.delete(id);
    }

    @Override
    public ItemDto create(ItemDto element, long userId) {
        log.info("Проверяем наличие пользователя с id {}", userId);
        userService.getById(userId);
        Item item = im.toItem(element);
        item.setOwner(um.toUser(userService.getById(userId)));
        log.info("Пользователь найден, обращаемся к хранилищу");
        return im.toItemDto(itemStorage.create(item));
    }

    @Override
    public ItemDto update(ItemDto element, long userId) {
        log.info("Проверяем наличие пользователя с id {}", userId);
        userService.getById(userId);
        log.info("Пользователь найден, проверяем наличие вещи с id {}", element.getId());
        //Получается мы теперь вместо вызова метода этого сервиса getById() должны обращаться к хранилищу и
        // обрабатывать ответ, т.е. делать то же что и метод getById. А данный метод не можем использовать потому тчо он
        //возвращает dto а там нет поля ownerId или его нужно добавить туда, но тесты не просят этого поля
        Item item = itemStorage.getById(element.getId()).orElseThrow(
                () -> {
                    log.warn("Вещь с id {} не найдена", element.getId());
                    return new NotFoundException(String.format("Сущность с id %d не найдена", element.getId()));
                });
        log.info("Вещь найдена, проверяем, что вещь с id {} принадлежит пользователю с id {}", element.getId(), userId);
        checkAuth(userId, item);
        if (element.getAvailable() != null) {
            log.info("Обновляем доступность на {}",element.getAvailable());
            item.setAvailable(element.getAvailable());
        }
        if (element.getDescription() != null) {
            log.info("Обновляем описание на {}", element.getDescription());
            item.setDescription(element.getDescription());
        }
        if (element.getName() != null) {
            log.info("Обновляем имя на {}", element.getName());
            item.setName(element.getName());
        }
        log.info("Обращаемся к хранилищу");
        return im.toItemDto(itemStorage.update(item));
    }

    @Override
    public Collection<ItemDto> search(String text) {
        if (text.isBlank()) {
            log.info("Переменная поиска пуста, возвращаем пустую коллекцию");
            return Collections.emptyList();
        }
        log.info("Обращаемся к хранилищу");
        return im.toItemDtoCollection(itemStorage.search(text));
    }

    private void checkAuth(Long userId, Item item) {
        if (item.getOwner().getId().equals(userId)) {
            log.info("Пользователь имеет доступа к этой вещи");
            return;
        }
        log.warn("Пользователь не имеет доступа к этой вещи");
        throw new NotAuthentication("Пользователь не имеет доступа к этой вещи");
    }
}
