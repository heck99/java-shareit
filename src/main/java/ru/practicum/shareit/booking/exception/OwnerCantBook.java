package ru.practicum.shareit.booking.exception;

public class OwnerCantBook extends RuntimeException {
    public OwnerCantBook(String message) {
        super(message);
    }
}
