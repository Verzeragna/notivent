package ru.notivent.controller;

import static org.springframework.http.ResponseEntity.*;
import static ru.notivent.util.HttpUtil.*;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.dto.GeoPointDto;
import ru.notivent.dto.GeoPointsDto;
import ru.notivent.dto.UserGeoPointDto;
import ru.notivent.enums.GradeType;
import ru.notivent.service.GeoPointService;

@RestController
@RequestMapping("geopoint")
@RequiredArgsConstructor
@ExtensionMethod(ResponseEntity.class)
public class GeoPointController {

  private final GeoPointService geoPointService;

  /**
   * Create geo point by user
   *
   * @param userUuid User UUID
   * @param dto Geo point data
   * @return Geo point data
   */
  @PostMapping
  public ResponseEntity<GeoPointDto> createGeoPoint(
      @RequestHeader(X_UUID) UUID userUuid, @RequestBody GeoPointDto dto) {
    return geoPointService.createGeoPoint(dto, userUuid);
  }

  /**
   * Get geo point data by ID
   *
   * @param uuid Geo point uuid
   * @return Geo point data
   */
  @GetMapping("{id}")
  public ResponseEntity<GeoPointDto> getGeoPointById(@PathVariable("id") UUID uuid) {
    return geoPointService.findGeoPointById(uuid);
  }

  /**
   * Get all user geo points and other public geo points
   *
   * @param userUuid User UUID
   * @param dto User current location data
   * @return List with user geo points and other public geo points
   */
  @PostMapping("getAll")
  public ResponseEntity<GeoPointsDto> getAllGeoPointsForUser(
      @RequestHeader(X_UUID) UUID userUuid, @RequestBody UserGeoPointDto dto) {
    return geoPointService.getAllGeoPointsForUser(dto, userUuid).ok();
  }

  /**
   * Delete geo point by ID
   *
   * @param userUuid User UUID
   * @param uuid Geo point uuid
   * @return Operation result
   */
  @PostMapping("delete/{id}")
  public ResponseEntity<Void> deleteGeoPoint(
      @RequestHeader(X_UUID) UUID userUuid, @PathVariable("id") UUID uuid) {
    geoPointService.deleteGeoPoint(userUuid, uuid);
    return ok().build();
  }

  /**
   * Check if geo point belongs current user
   * @param userUuid User UUID
   * @param uuid Geo point uuid
   * @return True or false
   */
  @PostMapping("{id}/belonguser")
  public ResponseEntity<Boolean> isGeoPointBelongUser(
      @RequestHeader(X_UUID) UUID userUuid, @PathVariable("id") UUID uuid) {
    return geoPointService.isGeoPointBelongUser(userUuid, uuid).ok();
  }

  @PostMapping("{id}/grade/{value}")
  public ResponseEntity<Integer> setGeoPointGrade(
          @RequestHeader(X_UUID) UUID userUuid, @PathVariable("id") UUID uuid, @PathVariable("value") GradeType gradeValue) {
    return geoPointService.setGeoPointGrade(userUuid, uuid, gradeValue);
  }

  @GetMapping("{id}/grade")
  public ResponseEntity<Integer> getGeoPointGrade(@PathVariable("id") UUID uuid) {
    return geoPointService.getGeoPointGrade(uuid);
  }
}
