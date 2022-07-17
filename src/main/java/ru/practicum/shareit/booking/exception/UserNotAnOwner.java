package ru.practicum.shareit.booking.exception;

public class UserNotAnOwner extends RuntimeException {
    public UserNotAnOwner(String message) {
        super(message);
    }
}
