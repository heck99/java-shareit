package ru.practicum.shareit.booking.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.StateStatus;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.exception.BookingDateException;
import ru.practicum.shareit.booking.exception.ItemNotAvailable;
import ru.practicum.shareit.booking.exception.UserNotAnOwner;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotAuthentication;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.UserNotBookedItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

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
        //TODO: убрать try catch когда сделают тесты
        try {
            userService.getById(userId);
        } catch (NotFoundException ex) {
            throw new UserNotBookedItem("");
        }
        ItemDto item = itemService.getById(element.getItemId());
        if (!item.getAvailable()) {
            throw new ItemNotAvailable("Вещь не доступна");
        }

        if (element.getEnd().isBefore(LocalDateTime.now())) {
            throw new BookingDateException("Дата окончания бронирования в прошлом");
        }

        if (element.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingDateException("Дата начала бронирования в прошлом");
        }

        Booking booking = bm.toBooking(element);
        booking.setBooker(new User(userId, null, null));
        booking.setItem(new Item(element.getItemId(), null, null, null, null, null, null));
        booking.setStatus(BookingStatus.WAITING);
        log.info("Обращаемся к хранилищу");
        return bm.toBookingPostDto(bookingRepository.save(booking));
    }

    @Override
    public void answerBooking(long bookingId, long userId, boolean approved) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        Booking booking = bookingOptional.orElseThrow(() ->
                new NotFoundException(String.format("Бронь с id = %d не существует", bookingId)));
        if (!(booking.getItem().getOwner().getId() == userId)) {
            throw new UserNotAnOwner("Пользователь не является владельцем вещи");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.save(booking);
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
        Collection<Booking> bookings = bookingRepository.findAllByBooker_IdOrderByStart(userId);
        return bm.toBookingDtoCollection(sortByState(bookings, state));
    }

    @Override
    public Collection<BookingGetDto> getAllByItemOwner(long userId, StateStatus state) {
        userService.getById(userId);
        Collection<Booking> bookings = bookingRepository.findAllByItem_Owner_IdOrderByStart(userId);
        return bm.toBookingDtoCollection(sortByState(bookings, state));
    }

    @Override
    public Collection<BookingGetDto> getByUserAndItem(long userId, long itemId) {
        itemService.getById(userId);
        userService.getById(userId);
        return bm.toBookingDtoCollection(bookingRepository.findAllByBooker_IdAndItem_Id(userId, itemId));
    }

    //Или лучше написать для каждого случая свой запрос и в зависимости от state вызывать нужный метод?
    private Collection<Booking> sortByState(Collection<Booking> bookings, StateStatus state) {

        switch (state) {
            case ALL:
                return new ArrayList<>(bookings);
            case PAST:
                return bookings.stream()
                        .filter((booking) -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookings.stream()
                        .filter((booking) -> booking.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case CURRENT:
                return bookings.stream()
                        .filter((booking) -> LocalDateTime.now().isAfter(booking.getStart())
                                && LocalDateTime.now().isBefore(booking.getEnd()))
                        .collect(Collectors.toList());
            case REJECTED:
                return bookings.stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.REJECTED))
                        .collect(Collectors.toList());
            case WAITING:
                return bookings.stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.WAITING))
                        .collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }

}
