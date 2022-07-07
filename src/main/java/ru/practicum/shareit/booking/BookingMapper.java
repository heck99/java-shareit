package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .status(BookingStatus.valueOf(bookingDto.getStatus()))
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(Item.builder()
                        .id(bookingDto.getItem().getId())
                        .name(bookingDto.getItem().getName())
                        .build())
                .broker(User.builder()
                        .id(bookingDto.getBroker().getId())
                        .name(bookingDto.getBroker().getName())
                        .build())
                .build();
    }

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .end(booking.getEnd())
                .start(booking.getStart())
                .id(booking.getId())
                .status(booking.getStatus().name())
                .item(BookingDto.Item.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .description(booking.getItem().getDescription())
                        .build())
                .broker(BookingDto.User.builder()
                        .id(booking.getBroker().getId())
                        .name(booking.getBroker().getName())
                        .build())
                .build();
    }
}
