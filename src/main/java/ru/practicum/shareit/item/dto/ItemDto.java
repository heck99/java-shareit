package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ItemDto {

    Long id;

    @NotBlank
    @NotNull
    String name;

    @NotNull
    String description;

    @NotNull
    Boolean available;

}
