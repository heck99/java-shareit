package ru.practicum.shareit.exception;

public class NotAuthentication extends RuntimeException {

    public NotAuthentication(String message) {
        super(message);
    }
}
