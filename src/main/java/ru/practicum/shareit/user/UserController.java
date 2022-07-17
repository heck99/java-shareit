package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public Collection<UserDto> getAll() {
        log.info("/GET");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserDto getElement(@PathVariable Long id) {
        log.info("/GET /{}", id);
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteElement(@PathVariable Long id) {
        log.info("/GET /{}", id);
        service.delete(id);
    }

    @PostMapping
    public UserDto createData(@Valid @RequestBody UserDto element) {
        log.info("/POST: " + element.toString());
        return service.create(element);
    }

    @PatchMapping("/{id}")
    public UserDto updateData(@RequestBody UserDto element, @PathVariable Long id) {
        log.info("/PATCH: " + element.toString());
        element.setId(id);
        return service.update(element);
    }
}
