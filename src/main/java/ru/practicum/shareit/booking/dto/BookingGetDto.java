package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingGetDto {

    private Long id;

    @NotNull
    @DateTimeFormat(fallbackPatterns = "yyy-MM-ddTHH:mm:ss")
    private LocalDateTime start;

    @NotNull
    @DateTimeFormat(fallbackPatterns = "yyy-MM-ddTHH:mm:ss")
    private LocalDateTime end;

    private Item item;

    private User booker;

    private String status;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private Long id;

        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {

        private Long id;

        private String name;

        private String description;

    }
}
