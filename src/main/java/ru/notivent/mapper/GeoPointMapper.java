package ru.notivent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.notivent.dto.ExistGeoPointDto;
import ru.notivent.dto.GeoPointDto;
import ru.notivent.model.GeoPoint;
import ru.notivent.model.GeoPointImage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GeoPointMapper {
    
    @Mapping(target = "images", source = "images", ignore = true)
    GeoPoint toModel(GeoPointDto dto);

    @Mapping(target = "images", source = "images", qualifiedByName = "getUrls")
    ExistGeoPointDto toDto(GeoPoint dto);

    @Mapping(target = "images", source = "images", ignore = true)
    GeoPoint toModel(ExistGeoPointDto dto);

    @Named("getUrls")
    default List<String> getUrls(List<GeoPointImage> images) {
        return images.stream().map(GeoPointImage::getImageUrl).collect(Collectors.toList());
    }

}
