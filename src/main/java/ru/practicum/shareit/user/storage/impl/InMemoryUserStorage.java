package ru.practicum.shareit.user.storage.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailIsNotUnique;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {

    private HashMap<Long, User> users;
    private long currentId = 1;

    public InMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public Optional<User> getById(long id) {
        User user = users.get(id);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user.toBuilder().build());
    }

    @Override
    public boolean delete(long id) {
        return users.remove(id) != null;
    }

    @Override
    public User create(User element) {
        checkEmail(element);
        element.setId(currentId++);
        users.put(element.getId(), element);
        return element.toBuilder().build();
    }

    @Override
    public User update(User element) {
        checkEmail(element);
        users.remove(element.getId());
        users.put(element.getId(), element);
        return element.toBuilder().build();
    }

    private boolean checkEmail(User user) {
        boolean notExists = users.values().stream()
                .filter(s -> s.getEmail().equals(user.getEmail()))
                .allMatch(s -> s.getId().equals(user.getId()));
        if (!notExists) {
            throw new EmailIsNotUnique("дынный email занят");
        }
        return true;
    }
}
