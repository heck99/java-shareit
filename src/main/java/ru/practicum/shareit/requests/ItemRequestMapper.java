package ru.practicum.shareit.requests;


import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;


public class ItemRequestMapper {
    public ItemRequest toItemRequest(ItemRequestDto itemDto) {
        ItemRequestDto.User user = itemDto.getRequest();
        return new ItemRequest(itemDto.getId(), itemDto.getDescription(),
                new User(user.getId(), user.getName(), null), itemDto.getCreated());
    }

    public ItemRequestDto toItemDto(ItemRequest itemRequest) {
        User user = itemRequest.getRequest();
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(),
                new ItemRequestDto.User(user.getId(), user.getName()), itemRequest.getCreated());
    }
}
