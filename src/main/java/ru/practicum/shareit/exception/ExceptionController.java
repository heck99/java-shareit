package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler({ValidationException.class})
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

    @ResponseBody
    @ExceptionHandler({NotAuthentication.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String onNotAuthenticationException(RuntimeException e) {
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<Violation> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
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
