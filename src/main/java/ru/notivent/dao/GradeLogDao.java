package ru.notivent.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.notivent.model.GradeLog;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GradeLogDao {

    void create(@Param("entity") GradeLog entity);

    Optional<GradeLog> findByGeoPointAndUser(@Param("pointUuid") UUID pointUuid,
                                             @Param("userUuid") UUID userUuid);

    void updateGradeType(@Param("entity") GradeLog entity);

    void delete(@Param("entity") GradeLog entity);
}
