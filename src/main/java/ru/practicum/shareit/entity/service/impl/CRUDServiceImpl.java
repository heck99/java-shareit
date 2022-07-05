package ru.practicum.shareit.entity.service.impl;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.entity.storage.CRUDStorage;
import ru.practicum.shareit.entity.model.Entity;
import ru.practicum.shareit.entity.service.CRUDService;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;

@Slf4j
public abstract class CRUDServiceImpl<T extends Entity, E extends CRUDStorage<T>> implements CRUDService<T> {

    protected E storage;

    public CRUDServiceImpl(E storage) {
        this.storage = storage;
    }

    @Override
    public Collection<T> getAll() {
        log.info("Обращаемся к хранилищу");
        return storage.getAll();
    }

    @Override
    public T getById(long id) {
        log.info("Обращаемся к хранилищу");
        return storage.getById(id).orElseThrow(
                () -> {
                    log.warn("Сущность с id {} не найдена", id);
                    return new NotFoundException(String.format("Сущность с id %d не найдена", id));
                });
    }

    @Override
    public boolean delete(long id) {
        log.info("Проверяем наличие сущности с id {}", id);
        getById(id);
        log.info("Сущность с id {} найдена, обращаемся к хранилищу для удаления", id);
        return storage.delete(id);
    }

    @Override
    public T create(T element) {
        log.info("Обращаемся к хранилищу");
        return storage.create(element);
    }

    @Override
    public T update(T element) {
        log.info("Обращаемся к хранилищу");
        return storage.update(element);
    }
}
