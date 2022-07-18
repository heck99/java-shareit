package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;
import java.util.stream.Collectors;

public class CommentMapper {

    public Comment toComment(CommentDto commentDto) {
        return new Comment(commentDto.getId(), commentDto.getText(), null, null, commentDto.getCreated());
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getUser().getName(), comment.getCreated());
    }

    public Collection<CommentDto> toCommentDtoCollection(Collection<Comment> comments) {
        return comments.stream().map(this::toCommentDto).collect(Collectors.toList());
    }
}
