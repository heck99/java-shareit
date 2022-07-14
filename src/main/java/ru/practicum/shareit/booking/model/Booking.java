package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.entity.model.Entity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class Booking extends Entity {
    @NotNull
    private LocalDate start;
    @NotNull
    private LocalDate end;
    @NotNull
    private Item item;
    @NotNull
    private User broker;
    @NotNull
    private BookingStatus status;
}
