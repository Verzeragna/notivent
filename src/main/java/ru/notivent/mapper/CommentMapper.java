package ru.notivent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.notivent.dto.CommentDto;
import ru.notivent.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "comment.user.nickName", target = "nickName")
    CommentDto toDto(Comment comment);
}
