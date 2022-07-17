package ru.practicum.shareit.item.exception;

public class UserNotBookedItem extends RuntimeException {
    public UserNotBookedItem(String message) {
        super(message);
    }
}
