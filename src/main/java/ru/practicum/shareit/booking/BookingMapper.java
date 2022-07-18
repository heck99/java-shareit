package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

public class BookingMapper {
    public Booking toBooking(BookingGetDto bookingDto) {
        Item item;
        if (bookingDto.getItem() != null) {
            item = new Item(bookingDto.getItem().getId(), bookingDto.getItem().getName(),
                    bookingDto.getItem().getDescription(), null, null, null, null);
        } else {
            item = null;
        }
        User user;
        if (bookingDto.getBooker() != null) {
            user = new User(bookingDto.getBooker().getId(), bookingDto.getBooker().getName(), null);
        } else {
            user = null;
        }

        return new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(),
                item, user, BookingStatus.valueOf(bookingDto.getStatus()));
    }

    public Booking toBooking(BookingPostDto bookingDto) {
        return new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(), null, null, null);
    }

    public BookingPostDto toBookingPostDto(Booking booking) {
        return new BookingPostDto(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItem().getId());
    }

    public BookingGetDto toBookingDto(Booking booking) {
        return new BookingGetDto(booking.getId(), booking.getStart(), booking.getEnd(),
                new BookingGetDto.Item(booking.getItem().getId(), booking.getItem().getName(), booking.getItem().getDescription()),
                new BookingGetDto.User(booking.getBooker().getId(), booking.getBooker().getName()), booking.getStatus().name());
    }

    public Collection<BookingGetDto> toBookingDtoCollection(Collection<Booking> bokings) {
        return bokings.stream().map(this::toBookingDto).collect(Collectors.toList());
    }
}
