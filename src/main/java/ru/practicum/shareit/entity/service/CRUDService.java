package ru.practicum.shareit.entity.service;

import java.util.Collection;

public interface CRUDService<T> {
    Collection<T> getAll();

    T getById(long id);

    boolean delete(long id);

    T create(T element);

    T update(T element);
}
