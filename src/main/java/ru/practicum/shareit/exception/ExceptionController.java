package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.exception.UserNotBookedItem;

import javax.validation.ValidationException;

@RestControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler({ValidationException.class, BookingDateException.class, ItemNotAvailable.class,
            MethodArgumentNotValidException.class, BookingNotWaitingStatus.class,
            EndDateBeforeStartDate.class, UserNotBookedItem.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onInternalServerError(RuntimeException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onNotFoundException(RuntimeException e) {
        return e.getMessage();
    }

    //TODO: заменить на  @ResponseStatus(HttpStatus.FORBIDDEN), когда доделают тесты
    @ResponseBody
    @ExceptionHandler({NotAuthentication.class, UserNotAnOwner.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onNotAuthenticationException(RuntimeException e) {
        return e.getMessage();
    }

    private static class Violation {
        private final String fieldName;
        private final String message;

        public String getFieldName() {
            return fieldName;
        }

        public String getMessage() {
            return message;
        }

        public Violation(String fieldName, String message) {
            this.fieldName = fieldName;
            this.message = message;
        }
    }
}
