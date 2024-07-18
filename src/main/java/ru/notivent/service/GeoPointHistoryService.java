package ru.notivent.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Service;
import ru.notivent.dao.GeoPointHistoryDao;

@Service
@RequiredArgsConstructor
public class GeoPointHistoryService {

    @Delegate
    private final GeoPointHistoryDao geoPointHistoryDao;
}
