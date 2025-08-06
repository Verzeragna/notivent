package ru.notivent.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.notivent.dao.GeoPointDao;
import ru.notivent.enums.GeoPointType;
import ru.notivent.model.GeoPoint;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeoPointRepositoryService {

    private final GeoPointDao geoPointDao;
    
    public boolean isPointsHaveAcceptableDistance(int distance, double longitude, double latitude, double userLongitude, double userLatitude) {
        return geoPointDao.isPointsHaveAcceptableDistance(distance, longitude, latitude, userLongitude, userLatitude);
    }
    
    public UUID create(GeoPoint geoPoint) {
        return geoPointDao.create(geoPoint);
    }

    @Cacheable(cacheNames = "geopoint", key = "#id")
    public Optional<GeoPoint> findById(UUID id) {
        log.info("Get geopoint from DB...");
        return geoPointDao.findById(id);
    }

    @CachePut(cacheNames = "geopoint", key = "#geoPoint.uuid")
    public void updateNameAndDescription(GeoPoint geoPoint) {
        geoPointDao.updateNameAndDescription(geoPoint);
    }

    public List<GeoPoint> findByUserAndType(UUID userId, GeoPointType geoPointType) {
        return geoPointDao.findByUserAndType(userId, geoPointType);
    }

    public List<GeoPoint> findAllByUserAndRadius(double longitude, double latitude, double radius, int limit) {
        return geoPointDao.findAllByUserAndRadius(longitude, latitude, radius, limit);
    }

    @CacheEvict(cacheNames = "geopoint", key = "#id")
    public void deleteById(UUID id) {
        geoPointDao.deleteById(id);
    }

    public void updateGrade(UUID id, int value) {
        geoPointDao.updateGrade(id, value);
    }
}
