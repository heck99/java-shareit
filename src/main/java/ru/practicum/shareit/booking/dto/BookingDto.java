package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.entity.model.Entity;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class BookingDto extends Entity {
    @NotNull
    private LocalDate start;
    @NotNull
    private LocalDate end;
    @NotNull
    private Item item;
    @NotNull
    private User broker;
    @NotNull
    private String status;

    @Getter
    @Setter
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    public static class User extends Entity {
        private String name;
    }

    @Getter
    @Setter
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    public static class Item extends Entity {
        private String name;
        private String description;
    }
}
