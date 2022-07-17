package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public Item toItem(ItemDto itemDto) {
        List<Comment> comments = itemDto.getComments().stream()
                .map((comment -> new Comment(comment.getId(), comment.getText(), null, null, null)))
                .collect(Collectors.toList());
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                null, null, comments);
    }

    public ItemDto toItemDto(Item item) {
        List<ItemDto.Comment> comments = item.getComments().stream()
                .map(comment -> new ItemDto.Comment(comment.getId(), comment.getText()))
                .collect(Collectors.toList());
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getOwner().getId(), comments);
    }

    public Collection<ItemDto> toItemDtoCollection(Collection<Item> items) {
        return items.stream().map(this::toItemDto).collect(Collectors.toList());
    }
}
