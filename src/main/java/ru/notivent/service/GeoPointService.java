package ru.notivent.service;

import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.notivent.dao.GeoPointDao;
import ru.notivent.dto.GeoPointDto;
import ru.notivent.dto.GeoPointsDto;
import ru.notivent.dto.UserGeoPointDto;
import ru.notivent.enums.GeoPointType;
import ru.notivent.enums.GradeType;
import ru.notivent.mapper.CommentMapper;
import ru.notivent.mapper.GeoPointMapper;
import ru.notivent.model.GeoPoint;
import ru.notivent.model.GradeLog;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoPointService {

  final GeoPointMapper geoPointMapper;
  final GeoPointHistoryService geoPointHistoryService;
  final CommentHistoryService commentHistoryService;
  final CommentService commentService;
  final SubscriptionService subscriptionService;
  final GradeLogService gradeLogService;
  final LocationService locationService;

  // TODO: This setting will be made by the user in the future
  private static final int MAX_POINTS_COUNT = 1000;

  // TODO: This setting will be made by the user in the future
  // We use geography function. In this case here we use distance in meters
  private static final double RADIUS = 30000.0;

  // distance in meters
  private static final int MAX_DISTANCE = 1000;

  @Delegate private final GeoPointDao geoPointDao;

  @Transactional
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
    val geoPointLive = dto.getCreatedAt().plusDays(7);
    geoPointModel.setLive(geoPointLive);
    geoPointModel.setUserUuid(userUuid);
    try {
      var location = locationService.findByAddressLine(geoPointModel.getLocation().getAddressLine());
      if (location.isPresent()) {
        geoPointModel.getLocation().setId(location.get().getId());
      } else {
        var locationId = locationService.save(geoPointModel.getLocation());
        geoPointModel.getLocation().setId(locationId);
      }
    } catch (Exception ex) {
      log.warn("Location is empty: user {}; coordinates: {}, {}", userUuid, geoPointModel.getLatitude(), geoPointModel.getLongitude());
    }

    return ResponseEntity.ok(geoPointMapper.toDto(create(geoPointModel)));
  }

  public ResponseEntity<GeoPointDto> findGeoPointById(UUID userUuid, UUID uuid) {
    var geoPoint = findById(uuid);
    if (geoPoint.isPresent()) {
      var point = geoPoint.get();
      if (Objects.equals(point.getType(), GeoPointType.PUBLIC)
          && !subscriptionService.isUserHasActiveSubscription(userUuid)) {
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
      }
      var pointDto = geoPointMapper.toDto(point);
      return ResponseEntity.ok(pointDto);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  public GeoPointsDto getAllGeoPointsForUser(UserGeoPointDto dto, UUID userUuid) {
    var privatePoints = findByUser(userUuid);
    val publicPointsCount = MAX_POINTS_COUNT - privatePoints.size();
    var publicPoints =
        findAllByUserAndRadius(dto.getLongitude(), dto.getLatitude(), RADIUS, publicPointsCount);
    privatePoints.addAll(publicPoints);
    return new GeoPointsDto(privatePoints.stream().map(geoPointMapper::toDto).toList());
  }

  @Transactional
  public void deleteGeoPoint(UUID userUuid, UUID geoPointUuid) {
    // Условие не удалять. Защита от дурака.
    if (isGeoPointBelongUser(userUuid, geoPointUuid)) {
      var geoPoint = findById(geoPointUuid);
      if (geoPoint.isPresent()) {
        geoPointHistoryService.create(geoPoint.get());
        var comments = commentService.findAllByGeoPoint(geoPointUuid);
        commentHistoryService.create(comments);
        commentService.delete(geoPointUuid);
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

  public ResponseEntity<Integer> setGeoPointGrade(
      UUID userUuid, UUID geoPointUuid, GradeType gradeValue) {
    var geoPoint = findById(geoPointUuid);
    if (geoPoint.isPresent()) {
      var point = geoPoint.get();
      if (!updateGradeLog(point, gradeValue)) return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
      var newGrade = updateGrade(point.getGrade(), gradeValue);
      updateGrade(geoPointUuid, newGrade);
      return ResponseEntity.ok(newGrade);
    }
    log.error("Geo point with UUID {} not found for user {}.", geoPointUuid, userUuid);
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  private Integer updateGrade(Integer grade, GradeType gradeValue) {
    if (gradeValue.equals(GradeType.PLUS)) {
      return grade + 1;
    }
    return grade - 1;
  }

  private boolean updateGradeLog(GeoPoint point, GradeType gradeValue) {
    var gradeLog = gradeLogService.findByGeoPointAndUser(point.getUuid(), point.getUserUuid());
    if (gradeLog.isPresent()) {
      var grade = gradeLog.get();
      if (grade.getGradeType().equals(gradeValue)) {
        return false;
      }
      gradeLogService.delete(grade);
      return true;
    }
    var newGradeLog =
        GradeLog.builder()
            .userUuid(point.getUserUuid())
            .geoPointUuid(point.getUuid())
            .gradeType(gradeValue)
            .build();
    gradeLogService.create(newGradeLog);
    return true;
  }

  public ResponseEntity<Integer> getGeoPointGrade(UUID geoPointUuid) {
    var geoPoint = findById(geoPointUuid);
    if (geoPoint.isPresent()) {
      return ResponseEntity.ok(geoPoint.get().getGrade());
    }
    log.error("Geo point with UUID {} not found.", geoPointUuid);
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}
