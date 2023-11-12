package ru.notivent.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.dto.GeoPointDto;
import ru.notivent.mapper.GeoPointMapper;
import ru.notivent.service.GeoPointService;

@RestController
@RequestMapping("geopoint")
@RequiredArgsConstructor
@ExtensionMethod(ResponseEntity.class)
public class GeoPointController {
    
    private final GeoPointService geoPointService;
    private final GeoPointMapper geoPointMapper;
    
    @PostMapping
    public ResponseEntity<GeoPointDto> createGeoPoint(@RequestBody GeoPointDto dto) {
        return geoPointMapper.toDto(geoPointService.createGeoPoint(geoPointMapper.toModel(dto))).ok();
    }
}
