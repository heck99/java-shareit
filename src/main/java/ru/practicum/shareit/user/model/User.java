package ru.practicum.shareit.user.model;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    Long id;
    private String name;
    private String email;
}
