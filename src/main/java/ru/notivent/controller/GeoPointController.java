package ru.notivent.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.dto.GeoPointDto;
import ru.notivent.dto.GeoPointsDto;
import ru.notivent.dto.UserGeoPointDto;
import ru.notivent.mapper.GeoPointMapper;
import ru.notivent.service.GeoPointService;

import java.util.UUID;

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

    @GetMapping("{id}")
    public ResponseEntity<GeoPointDto> getGeoPointById(@PathVariable("id") UUID uuid) {
        return geoPointMapper.toDto(geoPointService.findGeoPointById(uuid)).ok();
    }

    @PostMapping("getAll")
    public ResponseEntity<GeoPointsDto> getAllGeoPointsForUser(@RequestBody UserGeoPointDto dto) {
        return geoPointService.getAllGeoPointsForUser(dto).ok();
    }
}
