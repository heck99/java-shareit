package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {

    Collection<Item> getAllByUserId(long id);

    Optional<Item> getById(long id);

    boolean delete(long id);

    Item create(Item element);

    Item update(Item element);

    Collection<Item> search(String text);
}
