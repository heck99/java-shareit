package ru.practicum.shareit.requests.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.entity.model.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ItemRequestDto extends Entity {
    @NotNull
    private String description;
    @NotNull
    private User request;
    @NotNull
    private LocalDateTime created;

    public static User getNewUser() {
        return new User();
    }

    // не уверен, какой уровень доступа должен иметь данный класс и можно ли к нему обращаться через его билдер
    //или нужно в основном классе делать методы, который будут создавать экземпляр этого класса и геттеры и сеттеры для
    // него переносить в основной класс
    @Getter
    @Setter
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    public static class User extends Entity {
        private String name;
    }
}
