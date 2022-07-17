package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.StateStatus;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;

import java.util.Collection;

public interface BookingService {
    BookingPostDto create(BookingPostDto element, long userId);

    void answerBooking(long bookingId, long userId, boolean approved);

    BookingGetDto getById(long id, long userId);

    Collection<BookingGetDto> getAllByUser(long userId, StateStatus state);

    Collection<BookingGetDto> getAllByItemOwner(long userId, StateStatus state);

    Collection<BookingGetDto> getByUserAndItem(long userId, long itemId);
}
