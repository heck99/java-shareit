package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDto> getAllByUserId(long id);

    ItemDto getById(long id);

    boolean delete(long id, long userId);

    ItemDto create(ItemDto element, long userId);

    ItemDto update(ItemDto element, long userId);

    Collection<ItemDto> search(String text);
}
