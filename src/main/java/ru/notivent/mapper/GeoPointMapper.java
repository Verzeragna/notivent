package ru.notivent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.notivent.dto.GeoPointDto;
import ru.notivent.model.GeoPoint;

@Mapper(componentModel = "spring")
public interface GeoPointMapper {

    GeoPoint toModel(GeoPointDto dto);

    GeoPointDto toDto(GeoPoint dto);
}
