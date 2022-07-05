package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.entity.CRUDController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/users")
public class UserController extends CRUDController<User, UserService, UserDto> {
    private UserMapper um = new UserMapper();

    public UserController(UserService service) {
        super(service);
    }

    @Override
    public UserDto toDto(User element) {
        return um.toUserDto(element);
    }

    @Override
    public User fromDto(UserDto element) {
        return um.toUser(element);
    }
}
