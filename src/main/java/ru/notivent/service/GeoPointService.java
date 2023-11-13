package ru.notivent.service;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.notivent.dao.GeoPointDao;
import ru.notivent.exception.NotiventException;
import ru.notivent.model.GeoPoint;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GeoPointService {

    @Delegate
    private final GeoPointDao geoPointDao;

    public GeoPoint createGeoPoint(GeoPoint geoPoint) {
        val geoPointLive = geoPoint.getCreatedAt().plus(1, ChronoUnit.DAYS);
        geoPoint.setLive(geoPointLive);
        return create(geoPoint);
    }

    public GeoPoint findGeoPointById(UUID uuid) {
        var geoPoint = findById(uuid);
        if (geoPoint.isPresent()) return geoPoint.get();
        throw new NotiventException(HttpStatus.BAD_REQUEST);
    }
}
