package ru.notivent.dao;

import org.apache.ibatis.annotations.Param;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;
import ru.notivent.model.GeoPoint;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GeoPointDao {

  GeoPoint create(@Param("entity") GeoPoint geoPoint);

  void deleteById(@Param("uuid") UUID uuid);

  Optional<GeoPoint> findById(@Param("uuid") UUID uuid);

  List<GeoPoint> findByUser(@Param("userUuid") UUID userUuid);

  List<GeoPoint> findAllByUserAndRadius(
      @Param("longitude") double longitude,
      @Param("latitude") double latitude,
      @Param("radius") double radius,
      @Param("limit") int limit);

  boolean isPointsHaveAcceptableDistance(@Param("distance") int distance,
                                         @Param("longitude") double longitude,
                                         @Param("latitude") double latitude,
                                         @Param("userLongitude") double userLongitude,
                                         @Param("userLatitude") double userLatitude);
}
