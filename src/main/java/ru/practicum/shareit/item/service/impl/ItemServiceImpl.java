package ru.practicum.shareit.item.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotAuthentication;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Collections;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private ItemStorage itemStorage;
    private UserService userService;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    @Override
    public Collection<Item> getAllByUserId(long id) {
        log.info("Проверяем наличие пользователя с id {}", id);
        userService.getById(id);
        log.info("Пользователь найден обращаемся к хранилищу");
        return itemStorage.getAllByUserId(id);
    }

    @Override
    public Item getById(long id) {
        log.info("Обращаемся к хранилищу");
        return itemStorage.getById(id).orElseThrow(
                () -> {
                    log.warn("Вещь с id {} не найдена", id);
                    return new NotFoundException(String.format("Сущность с id %d не найдена", id));
                });
    }

    @Override
    public boolean delete(long id, long userId) {
        log.info("Проверяем наличие пользователя с id {}", userId);
        userService.getById(userId);
        log.info("Пользователь найден, проверяем наличие вещи с id {}", id);
        Item item = getById(id);
        log.info("Вещь найдена, проверяем, что вещь с id {} принадлежит пользователю с id {}", id, userId);
        checkAuth(userId, item);
        log.info("Обращаемся к хранилищу");
        return itemStorage.delete(id);
    }

    @Override
    public Item create(Item element, long userId) {
        log.info("Проверяем наличие пользователя с id {}", userId);
        userService.getById(userId);
        element.setOwnerId(userId);
        log.info("Пользователь найден, обращаемся к хранилищу");
        return itemStorage.create(element);
    }

    @Override
    public Item update(Item element, long userId) {
        log.info("Проверяем наличие пользователя с id {}", userId);
        userService.getById(userId);
        log.info("Пользователь найден, проверяем наличие вещи с id {}", element.getId());
        Item item = getById(element.getId());
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
        return itemStorage.update(item);
    }

    @Override
    public Collection<Item> search(String text) {
        if (text.isBlank()) {
            log.info("Переменная поиска пуста, возвращаем пустую коллекцию");
            return Collections.emptyList();
        }
        log.info("Обращаемся к хранилищу");
        return itemStorage.search(text);
    }

    private boolean checkAuth(Long userId, Item item) {
        if (item.getOwnerId().equals(userId)) {
            log.info("Пользователь имеет доступа к этой вещи");
            return true;
        }
        log.warn("Пользователь не имеет доступа к этой вещи");
        throw new NotAuthentication("Пользователь не имеет доступа к этой вещи");
    }
}
