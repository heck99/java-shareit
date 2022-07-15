package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Item {

    Long id;

    private String name;

    private String description;

    private Boolean available;

    private User owner;

    private ItemRequest request;

}
