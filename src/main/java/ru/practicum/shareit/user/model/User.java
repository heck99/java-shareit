package ru.practicum.shareit.user.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.entity.model.Entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class User extends Entity {
    @NotBlank
    private String name;
    @Email
    private String email;
}
