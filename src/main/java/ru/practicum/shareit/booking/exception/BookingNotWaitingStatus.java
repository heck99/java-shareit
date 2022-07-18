package ru.practicum.shareit.booking.exception;

public class BookingNotWaitingStatus extends RuntimeException {
    public BookingNotWaitingStatus(String message) {
        super(message);
    }
}
