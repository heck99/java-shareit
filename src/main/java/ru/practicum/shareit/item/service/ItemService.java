package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Collection<Item> getAllByUserId(long id);

    Item getById(long id);

    boolean delete(long id, long userId);

    Item create(Item element, long userId);

    Item update(Item element, long userId);

    Collection<Item> search(String text);
}
