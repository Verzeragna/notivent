package ru.notivent.mapper;

import org.mapstruct.Mapper;
import ru.notivent.dto.NotificationDto;
import ru.notivent.model.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDto toDto(Notification model);
}
