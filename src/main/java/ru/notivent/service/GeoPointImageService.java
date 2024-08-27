package ru.notivent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.notivent.dao.GeoPointImageDao;
import ru.notivent.model.GeoPointImage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GeoPointImageService {
    final GeoPointImageDao geoPointImageDao;

    public void save(Collection<GeoPointImage> images) {
        if (!CollectionUtils.isEmpty(images)) {
            geoPointImageDao.save(images);
        }
    }
}
