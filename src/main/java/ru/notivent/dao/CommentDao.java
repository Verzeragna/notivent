package ru.notivent.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.notivent.model.Comment;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentDao {
    List<Comment> findAllByGeoPoint(@Param("uuid") UUID uuid);

    Integer countCommentsByGeoPoint(@Param("uuid") UUID uuid);

    void create(@Param("entity") Comment entity);
    void delete(@Param("uuid") UUID geoPointUuid);
}
