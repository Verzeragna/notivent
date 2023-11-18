package ru.notivent.service;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Service;
import ru.notivent.dao.GeoPointHistoryDao;

@Service
@AllArgsConstructor
public class GeoPointHistoryService {

    @Delegate
    private final GeoPointHistoryDao geoPointHistoryDao;
}
