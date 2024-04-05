package ru.notivent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.notivent.dao.CommentHistoryDao;
import ru.notivent.model.Comment;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentHistoryService {

    final CommentHistoryDao commentHistoryDao;

    public void create(List<Comment> comments) {
        if (!comments.isEmpty()) {
            commentHistoryDao.create(comments);
        }
    }
}
