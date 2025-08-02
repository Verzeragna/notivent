package ru.notivent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.notivent.dto.CommentDto;
import ru.notivent.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "profileImage", source = "comment.user.profileImage")
    @Mapping(target = "nickName", source = "comment.user.userName")
    CommentDto toDto(Comment comment);
}
