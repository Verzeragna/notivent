package ru.notivent.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.notivent.model.GeoPoint;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GeoPointDao {

    GeoPoint create(@Param("entity") GeoPoint geoPoint);

    Optional<GeoPoint> findById(@Param("uuid") UUID uuid);
}
