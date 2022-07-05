package ru.practicum.shareit.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.entity.service.impl.CRUDServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@Slf4j
public class UserServiceImpl extends CRUDServiceImpl<User, UserStorage> implements UserService {
    @Autowired
    public UserServiceImpl(UserStorage storage) {
        super(storage);
    }

    @Override
    public User update(User element) {
        log.info("Проверяем наличие пользователя с id {}", element.getId());
        User userForUpdate = getById(element.getId());
        log.info("Пользователь найден");
        if (element.getName() != null) {
            log.info("Обновляем имя на {}", element.getName());
            userForUpdate.setName(element.getName());
        }
        if (element.getEmail() != null) {
            log.info("Обновляем email на {}", element.getEmail());
            userForUpdate.setEmail(element.getEmail());
        }
        return super.update(userForUpdate);
    }
}
