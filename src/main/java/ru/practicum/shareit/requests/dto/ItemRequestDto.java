package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    Long id;

    @NotNull
    private String description;

    @NotNull
    private User request;

    @NotNull
    private LocalDateTime created;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        Long id;

        private String name;

    }
}
