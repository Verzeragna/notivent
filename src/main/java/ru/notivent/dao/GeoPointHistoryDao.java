package ru.notivent.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.notivent.model.GeoPoint;

@Repository
public interface GeoPointHistoryDao {

    void create(@Param("entity") GeoPoint geoPoint);
}
