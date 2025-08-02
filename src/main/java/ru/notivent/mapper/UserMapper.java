package ru.notivent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.notivent.dto.UserProfileDto;
import ru.notivent.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "email", source = "login")
    UserProfileDto toProfileDto(User model);
}
