package ru.notivent.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.notivent.model.GeoPointImage;

import java.util.Collection;

@Repository
public interface GeoPointImageDao {
    void save(@Param("images") Collection<GeoPointImage> images);
}
