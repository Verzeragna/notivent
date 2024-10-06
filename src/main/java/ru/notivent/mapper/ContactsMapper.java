package ru.notivent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.notivent.dto.ContactsAddDto;
import ru.notivent.dto.ContactsDto;
import ru.notivent.model.Contacts;
import ru.notivent.model.User;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ContactsMapper {
    @Mapping(target = "id", source = "user.uuid")
    ContactsDto toDto(User user);

    Contacts toModel(ContactsAddDto dto);

    @Mapping(target = "subUserId", source = "subUser.uuid")
    Contacts toModelFromUser(User subUser, UUID mainUserId);
}
