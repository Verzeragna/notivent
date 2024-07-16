package ru.notivent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.notivent.dto.CommentDto;
import ru.notivent.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "comment.user.userName", target = "userName")
    CommentDto toDto(Comment comment);
}
