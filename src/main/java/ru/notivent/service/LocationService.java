package ru.notivent.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Service;
import ru.notivent.dao.LocationDao;

@Service
@RequiredArgsConstructor
public class LocationService {

    @Delegate
    final LocationDao locationDao;
}
