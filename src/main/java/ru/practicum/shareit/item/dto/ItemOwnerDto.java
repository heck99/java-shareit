package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ItemOwnerDto {

    Long id;

    @NotBlank
    @NotNull
    String name;

    @NotNull
    String description;

    @NotNull
    Boolean available;

    Long ownerId;

    List<Comment> comments = new ArrayList<>();

    BookingGetDto last;

    BookingGetDto next;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comment {
        Long id;
        String text;
        String authorName;
    }

}
