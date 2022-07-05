package ru.practicum.shareit.entity.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;

@AllArgsConstructor()
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class Entity {
    @Min(1)
    private Long id;
}
