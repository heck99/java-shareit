package ru.practicum.shareit.booking.exception;

public class EndDateBeforeStartDate extends RuntimeException {
    public EndDateBeforeStartDate(String message) {
        super(message);
    }
}
