package ru.practicum.shareit.item.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotAuthentication;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.exception.UserNotBookedItem;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemStorage;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final BookingService bookingService;
    private final ItemMapper im = new ItemMapper();
    private final UserMapper um = new UserMapper();
    private final CommentMapper cm = new CommentMapper();

    @Autowired
    public ItemServiceImpl(ItemRepository itemStorage, UserService userService,
                           CommentRepository commentRepository, @Lazy BookingService bookingService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.bookingService = bookingService;
    }

    @Override
    public Collection<ItemDto> getAllByUserId(long id) {
        log.info("Проверяем наличие пользователя с id {}", id);
        User user = um.toUser(userService.getById(id));
        log.info("Пользователь найден обращаемся к хранилищу");
        List<Item> items = itemStorage.findAllByOwner(user);
        List<ItemDto> list = new ArrayList<>();
        for (Item item :items) {
            Optional<BookingGetDto> last = bookingService.getLastOwnerBooking(item.getOwner().getId());
            Optional<BookingGetDto> next = bookingService.getNextOwnerBooking(item.getOwner().getId());
            list.add(im.toItemDtoOwner(item, last.orElse(null), next.orElse(null)));
        }
        return list;
    }

    @Override
    public ItemDto getById(long id, long userId) {
        log.info("Обращаемся к хранилищу");
        Item item = itemStorage.findById(id).orElseThrow(
                () -> {
                    log.warn("Вещь с id {} не найдена", id);
                    return new NotFoundException(String.format("Сущность с id %d не найдена", id));
                });
        if (!(item.getOwner().getId() == userId)) {
            return im.toItemDto(item);
        }
        Optional<BookingGetDto> last = bookingService.getLastOwnerBooking(item.getOwner().getId());
        Optional<BookingGetDto> next = bookingService.getNextOwnerBooking(item.getOwner().getId());
        return im.toItemDtoOwner(item, last.orElse(null), next.orElse(null));
    }

    @Override
    public void delete(long id, long userId) {
        log.info("Проверяем наличие пользователя с id {}", userId);
        userService.getById(userId);
        log.info("Пользователь найден, проверяем наличие вещи с id {}", id);
        Item item = im.toItem(getById(id, userId));
        log.info("Вещь найдена, проверяем, что вещь с id {} принадлежит пользователю с id {}", id, userId);
        checkAuth(userId, item);
        log.info("Обращаемся к хранилищу");
        itemStorage.deleteById(id);
    }

    @Override
    public ItemDto create(ItemDto element, long userId) {
        log.info("Проверяем наличие пользователя с id {}", userId);
        userService.getById(userId);
        Item item = im.toItem(element);
        item.setOwner(um.toUser(userService.getById(userId)));
        log.info("Пользователь найден, обращаемся к хранилищу");
        return im.toItemDto(itemStorage.save(item));
    }

    @Override
    public ItemDto update(ItemDto element, long userId) {
        log.info("Проверяем наличие пользователя с id {}", userId);
        userService.getById(userId);
        log.info("Пользователь найден, проверяем наличие вещи с id {}", element.getId());
        Item item = itemStorage.findById(element.getId()).orElseThrow(
                () -> {
                    log.warn("Вещь с id {} не найдена", element.getId());
                    return new NotFoundException(String.format("Сущность с id %d не найдена", element.getId()));
                });
        log.info("Вещь найдена, проверяем, что вещь с id {} принадлежит пользователю с id {}", element.getId(), userId);
        checkAuth(userId, item);
        if (element.getAvailable() != null) {
            log.info("Обновляем доступность на {}", element.getAvailable());
            item.setAvailable(element.getAvailable());
        }
        if (element.getDescription() != null) {
            log.info("Обновляем описание на {}", element.getDescription());
            item.setDescription(element.getDescription());
        }
        if (element.getName() != null) {
            log.info("Обновляем имя на {}", element.getName());
            item.setName(element.getName());
        }
        log.info("Обращаемся к хранилищу");
        return im.toItemDto(itemStorage.save(item));
    }

    @Override
    public Collection<ItemDto> search(String text) {
        if (text.isBlank()) {
            log.info("Переменная поиска пуста, возвращаем пустую коллекцию");
            return Collections.emptyList();
        }
        log.info("Обращаемся к хранилищу");
        return im.toItemDtoCollection(itemStorage.search(text));
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, long userId, long itemId) {
        User user = um.toUser(userService.getById(userId));
        Item item = im.toItem(getById(itemId, userId));
        Collection<BookingGetDto> bookings = bookingService.getByUserAndItem(userId, itemId);
        if (bookings.size() == 0) {
            throw new UserNotBookedItem("Пользователь не арендовал этот предмет");
        }
        if (bookings.stream().allMatch((booking) -> booking.getStart().isAfter(LocalDateTime.now()))) {
            throw new UserNotBookedItem("Пользователь ещё не арендовал этот предмет");
        }
        Comment comment = cm.toComment(commentDto);
        comment.setItem(item);
        comment.setUser(user);
        comment.setCreated(LocalDateTime.now());
        return cm.toCommentDto(commentRepository.save(comment));
    }

    private void checkAuth(Long userId, Item item) {
        if (item.getOwner().getId().equals(userId)) {
            log.info("Пользователь имеет доступа к этой вещи");
            return;
        }
        log.warn("Пользователь не имеет доступа к этой вещи");
        throw new NotAuthentication("Пользователь не имеет доступа к этой вещи");
    }
}
