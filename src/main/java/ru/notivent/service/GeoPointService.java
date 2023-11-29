package ru.notivent.service;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import lombok.val;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.notivent.dao.GeoPointDao;
import ru.notivent.dao.GeoPointHistoryDao;
import ru.notivent.dto.GeoPointDto;
import ru.notivent.dto.GeoPointsDto;
import ru.notivent.dto.UserGeoPointDto;
import ru.notivent.enums.GeoPointType;
import ru.notivent.exception.DistanceAcceptableException;
import ru.notivent.exception.NotiventException;
import ru.notivent.exception.SubscriptionException;
import ru.notivent.mapper.GeoPointMapper;
import ru.notivent.model.GeoPoint;

import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

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

  public ResponseEntity<GeoPointDto> createGeoPoint(GeoPointDto dto, UUID userUuid) throws SubscriptionException, DistanceAcceptableException  {
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

  public GeoPoint findGeoPointById(UUID uuid) {
    var geoPoint = findById(uuid);
    if (geoPoint.isPresent()) return geoPoint.get();
    throw new NotiventException(HttpStatus.BAD_REQUEST);
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
      val geoPoint = findGeoPointById(geoPointUuid);
      geoPointHistoryService.create(geoPoint);
      deleteById(geoPointUuid);
    }
  }

  public Boolean isGeoPointBelongUser(UUID userUuid, UUID geoPointUuid) {
    val geoPoint = findGeoPointById(geoPointUuid);
    return Objects.equals(geoPoint.getUserUuid(), userUuid);
  }
}
