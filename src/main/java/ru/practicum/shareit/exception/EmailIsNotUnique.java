package ru.practicum.shareit.exception;

public class EmailIsNotUnique extends RuntimeException {
    public EmailIsNotUnique(String message) {
        super(message);
    }
}
