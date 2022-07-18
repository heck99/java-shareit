package ru.practicum.shareit.booking.exception;

public class ItemNotAvailable extends RuntimeException {
    public ItemNotAvailable(String message) {
        super(message);
    }
}
