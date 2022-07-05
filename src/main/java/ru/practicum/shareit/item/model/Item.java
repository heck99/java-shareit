package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.entity.model.Entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder(toBuilder = true)
public class Item extends Entity {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Boolean available;
    private Long ownerId;
    private Long requestId;
}
