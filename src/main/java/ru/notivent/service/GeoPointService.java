package ru.notivent.service;

import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.notivent.dao.GeoPointDao;
import ru.notivent.dto.GeoPointDto;
import ru.notivent.dto.GeoPointsDto;
import ru.notivent.dto.UserGeoPointDto;
import ru.notivent.enums.GeoPointType;
import ru.notivent.mapper.GeoPointMapper;

@Slf4j
@Service
@AllArgsConstructor
public class GeoPointService {

  private final GeoPointMapper geoPointMapper;
  private final GeoPointHistoryService geoPointHistoryService;
  private final SubscriptionService subscriptionService;

  // TODO: This setting will be made by the user in the future
  private static final int MAX_POINTS_COUNT = 1000;

  // TODO: This setting will be made by the user in the future
  // We use geography function. In this case here we use distance in meters
  private static final double RADIUS = 10000.0;

  // distance in meters
  private static final int MAX_DISTANCE = 1000;

  @Delegate private final GeoPointDao geoPointDao;

  public ResponseEntity<GeoPointDto> createGeoPoint(GeoPointDto dto, UUID userUuid) {
    if (Objects.equals(dto.getType(), GeoPointType.PUBLIC)
        && !subscriptionService.isUserHasActiveSubscription(userUuid)) {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    var isAcceptable =
        isPointsHaveAcceptableDistance(
            MAX_DISTANCE,
            dto.getLongitude(),
            dto.getLatitude(),
            dto.getUserLongitude(),
            dto.getUserLatitude());
    if (!isAcceptable) return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    var geoPointModel = geoPointMapper.toModel(dto);
    val geoPointLive = dto.getCreatedAt().plus(1, ChronoUnit.DAYS);
    geoPointModel.setLive(geoPointLive);
    geoPointModel.setUserUuid(userUuid);
    return ResponseEntity.ok(geoPointMapper.toDto(create(geoPointModel)));
  }

  public ResponseEntity<GeoPointDto> findGeoPointById(UUID uuid) {
    var geoPoint = findById(uuid);
    return geoPoint
        .map(point -> ResponseEntity.ok(geoPointMapper.toDto(point)))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  public GeoPointsDto getAllGeoPointsForUser(UserGeoPointDto dto, UUID userUuid) {
    var privatePoints = findByUser(userUuid);
    val publicPointsCount = MAX_POINTS_COUNT - privatePoints.size();
    var publicPoints =
        findAllByUserAndRadius(dto.getLongitude(), dto.getLatitude(), RADIUS, publicPointsCount);
    privatePoints.addAll(publicPoints);
    return new GeoPointsDto(privatePoints.stream().map(geoPointMapper::toDto).toList());
  }

  public void deleteGeoPoint(UUID userUuid, UUID geoPointUuid) {
    // Условие не удалять. Защита от дурака.
    if (isGeoPointBelongUser(userUuid, geoPointUuid)) {
      var geoPoint = findById(geoPointUuid);
      if (geoPoint.isPresent()) {
        geoPointHistoryService.create(geoPoint.get());
        deleteById(geoPointUuid);
      } else {
        log.error("Geo point with UUID {} not found for user {}.", geoPointUuid, userUuid);
      }
    }
  }

  public Boolean isGeoPointBelongUser(UUID userUuid, UUID geoPointUuid) {
    var geoPoint = findById(geoPointUuid);
    if (geoPoint.isPresent()) {
      return Objects.equals(geoPoint.get().getUserUuid(), userUuid);
    }
    log.error("Geo point with UUID {} not found for user {}.", geoPointUuid, userUuid);
    return false;
  }
}
