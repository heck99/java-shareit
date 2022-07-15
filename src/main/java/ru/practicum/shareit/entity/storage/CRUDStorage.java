package ru.practicum.shareit.entity.storage;


import java.util.Collection;
import java.util.Optional;

public interface CRUDStorage<T> {
    Collection<T> getAll();

    Optional<T> getById(long id);

    boolean delete(long id);

    T create(T element);

    T update(T element);
}
