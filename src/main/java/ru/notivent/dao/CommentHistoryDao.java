package ru.notivent.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.notivent.model.Comment;

import java.util.List;

@Repository
public interface CommentHistoryDao {

    void create(@Param("comments") List<Comment> comments);
}
