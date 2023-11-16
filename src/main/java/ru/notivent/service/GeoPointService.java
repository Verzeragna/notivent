package ru.notivent.service;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import lombok.val;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.notivent.dao.GeoPointDao;
import ru.notivent.dto.GeoPointsDto;
import ru.notivent.dto.UserGeoPointDto;
import ru.notivent.exception.NotiventException;
import ru.notivent.mapper.GeoPointMapper;
import ru.notivent.model.GeoPoint;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GeoPointService {

  private final GeoPointMapper geoPointMapper;

  // TODO: This setting will be made by the user in the future
  private static final int MAX_POINTS_COUNT = 1000;

  // TODO: This setting will be made by the user in the future
  // We use geography function. In this case here we use distance in meters
  private static final double RADIUS = 10000.0;

  @Delegate private final GeoPointDao geoPointDao;

  public GeoPoint createGeoPoint(GeoPoint geoPoint, UUID userUuid) {
    val geoPointLive = geoPoint.getCreatedAt().plus(1, ChronoUnit.DAYS);
    geoPoint.setLive(geoPointLive);
    geoPoint.setUserUuid(userUuid);
    val createdPoint = create(geoPoint);
    return createdPoint;
  }

  public GeoPoint findGeoPointById(UUID uuid) {
    var geoPoint = findById(uuid);
    if (geoPoint.isPresent()) return geoPoint.get();
    throw new NotiventException(HttpStatus.BAD_REQUEST);
  }

  public GeoPointsDto getAllGeoPointsForUser(UserGeoPointDto dto, UUID userUuid) {
    var privatePoints = findByUser(userUuid);
    val publicPointsCount = MAX_POINTS_COUNT - privatePoints.size();
    var publicPoints = findAllByUserAndRadius(dto.getLongitude(), dto.getLatitude(), RADIUS, publicPointsCount);
    privatePoints.addAll(publicPoints);
    return new GeoPointsDto(privatePoints.stream().map(geoPointMapper::toDto).toList());
  }
}
