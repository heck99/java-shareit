package ru.practicum.shareit.entity.storage;

import ru.practicum.shareit.entity.model.Entity;

import java.util.Collection;
import java.util.Optional;

public interface CRUDStorage<T extends Entity> {
    Collection<T> getAll();

    Optional<T> getById(long id);

    boolean delete(long id);

    T create(T element);

    T update(T element);
}
