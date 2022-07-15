package ru.practicum.shareit.item.storage.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final HashMap<Long, Item> items;
    private long currentId = 1;

    public InMemoryItemStorage() {
        items = new HashMap<>();
    }

    @Override
    public Collection<Item> getAllByUserId(long id) {
        return items.values().stream().filter(s -> s.getOwner().getId() == id).collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getById(long id) {
        Item item = items.get(id);
        if (item == null) {
            return Optional.empty();
        }
        return Optional.of(item);
    }

    @Override
    public boolean delete(long id) {
        return items.remove(id) != null;
    }

    @Override
    public Item create(Item element) {
        element.setId(currentId++);
        items.put(element.getId(), element);
        return element;
    }

    @Override
    public Item update(Item element) {
        items.remove(element.getId());
        items.put(element.getId(), element);
        return element;
    }

    @Override
    public Collection<Item> search(String text) {
        return items.values().stream()
                .filter(s -> (s.getName().toLowerCase().contains(text.toLowerCase())
                                || s.getDescription().toLowerCase().contains(text.toLowerCase()))
                                && s.getAvailable()
                        )
                .collect(Collectors.toList());
    }
}
