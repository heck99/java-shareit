package ru.practicum.shareit.requests;


import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;


public class ItemRequestMapper {
    public ItemRequest toItem(ItemRequestDto itemDto) {
        ItemRequestDto.User user = itemDto.getRequest();

        return ItemRequest.builder()
                .description(itemDto.getDescription())
                .id(itemDto.getId())
                .created(itemDto.getCreated())
                .request(User.builder()
                        .name(user.getName())
                        .id(user.getId())
                        .build())
                .build();
    }

    public ItemRequestDto toItemDto(ItemRequest itemRequest) {
        User user = itemRequest.getRequest();
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .request(ItemRequestDto.User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build())
                .build();
    }
}
