package ru.practicum.shareit.booking.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.StateStatus;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotAuthentication;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserService userService;

    private final ItemService itemService;

    private static final BookingMapper bm = new BookingMapper();

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public BookingPostDto create(BookingPostDto element, long userId) {
        userService.getById(userId);
        ItemDto item = itemService.getById(element.getItemId(), userId);
        if (!item.getAvailable()) {
            throw new ItemNotAvailable("Вещь не доступна");
        }

        if (element.getEnd().isBefore(LocalDateTime.now())) {
            throw new BookingDateException("Дата окончания бронирования в прошлом");
        }

        if (element.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingDateException("Дата начала бронирования в прошлом");
        }

        if (element.getStart().isAfter(element.getEnd())) {
            throw new EndDateBeforeStartDate("Дата окончания бронирования раньше даты начала бронирования");
        }

        if (item.getOwnerId() == userId) {
            throw new NotFoundException("Владелец вещи не может её арендовать");
        }
        Booking booking = bm.toBooking(element);
        booking.setBooker(new User(userId, null, null));
        booking.setItem(new Item(element.getItemId(), null, null, null, null, null, null));
        booking.setStatus(BookingStatus.WAITING);
        log.info("Обращаемся к хранилищу");
        return bm.toBookingPostDto(bookingRepository.save(booking));
    }

    @Override
    public BookingGetDto answerBooking(long bookingId, long userId, boolean approved) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        Booking booking = bookingOptional.orElseThrow(() ->
                new NotFoundException(String.format("Бронь с id = %d не существует", bookingId)));
        if (!(booking.getItem().getOwner().getId() == userId)) {
            throw new UserNotAnOwner("Пользователь не является владельцем вещи");
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new BookingNotWaitingStatus("Бронирование не ожидает подтверждения");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bm.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingGetDto getById(long id, long userId) {
        userService.getById(userId);
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        Booking booking = bookingOptional.orElseThrow(() -> new NotFoundException(String.format("Сущность с id %d не существует", id)));
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            throw new NotAuthentication("Пользователь не имеет доступа к этой записи");
        }
        return bm.toBookingDto(booking);
    }

    @Override
    public Collection<BookingGetDto> getAllByUser(long userId, StateStatus state) {
        userService.getById(userId);
        switch (state) {
            case ALL:
                return bm.toBookingDtoCollection(bookingRepository.findAllByBooker_IdOrderByStartDesc(userId));
            case PAST:
                return bm.toBookingDtoCollection(bookingRepository.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(userId,
                        LocalDateTime.now()));
            case FUTURE:
                return bm.toBookingDtoCollection(bookingRepository.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(userId,
                        LocalDateTime.now()));
            case CURRENT:
                return bm.toBookingDtoCollection(
                        bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                                LocalDateTime.now(), LocalDateTime.now())
                );
            case REJECTED:
                return bm.toBookingDtoCollection(bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(userId,
                        BookingStatus.REJECTED)
                );
            case WAITING:
                return bm.toBookingDtoCollection(bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(userId,
                        BookingStatus.WAITING)
                );
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public Collection<BookingGetDto> getAllByItemOwner(long userId, StateStatus state) {
        userService.getById(userId);
        switch (state) {
            case ALL:
                return bm.toBookingDtoCollection(bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId));
            case PAST:
                return bm.toBookingDtoCollection(bookingRepository.findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(userId,
                        LocalDateTime.now()));
            case FUTURE:
                return bm.toBookingDtoCollection(bookingRepository.findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(userId,
                        LocalDateTime.now()));
            case CURRENT:
                return bm.toBookingDtoCollection(
                        bookingRepository.findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                                LocalDateTime.now(), LocalDateTime.now())
                );
            case REJECTED:
                return bm.toBookingDtoCollection(bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(userId,
                        BookingStatus.REJECTED)
                );
            case WAITING:
                return bm.toBookingDtoCollection(bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(userId,
                        BookingStatus.WAITING)
                );
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public Collection<BookingGetDto> getByUserAndItem(long userId, long itemId) {
        itemService.getById(itemId, userId);
        userService.getById(userId);
        return bm.toBookingDtoCollection(bookingRepository.findItemBookingFromUser(userId, itemId));
    }

    @Override
    public Optional<BookingGetDto> getLastOwnerBooking(long ownerId) {
        userService.getById(ownerId);
        Optional<Booking> bookingOptional = bookingRepository.findFirstByItem_Owner_IdAndEndBeforeAndStatusOrderByEndDesc(ownerId,
                LocalDateTime.now(), BookingStatus.APPROVED);
        if (bookingOptional.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(bm.toBookingDto(bookingOptional.get()));
    }

    @Override
    public Optional<BookingGetDto> getNextOwnerBooking(long ownerId) {
        userService.getById(ownerId);
        Optional<Booking> bookingOptional = bookingRepository.findFirstByItem_Owner_IdAndStartAfterAndStatusOrderByEndAsc(ownerId,
                LocalDateTime.now(), BookingStatus.APPROVED);
        if (bookingOptional.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(bm.toBookingDto(bookingOptional.get()));
    }
}
