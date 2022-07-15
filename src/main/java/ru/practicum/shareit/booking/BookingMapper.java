package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public Booking toBooking(BookingDto bookingDto) {
        return new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(),
                new Item(bookingDto.getItem().getId(), bookingDto.getItem().getName(),
                        bookingDto.getItem().getDescription(), null, null, null),
                new User(bookingDto.getBroker().getId(), bookingDto.getBroker().getName(),
                        null), BookingStatus.valueOf(bookingDto.getStatus()));
    }

    public BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(),
                new BookingDto.Item(booking.getItem().getId(), booking.getItem().getName(), booking.getItem().getDescription()),
                new BookingDto.User(booking.getBroker().getId(), booking.getBroker().getName()), booking.getStatus().name());
    }
}
