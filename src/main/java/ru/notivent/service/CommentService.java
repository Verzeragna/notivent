package ru.notivent.service;

import static java.util.Comparator.comparing;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.notivent.dao.CommentDao;
import ru.notivent.dao.CommentPostDto;
import ru.notivent.dto.CommentDto;
import ru.notivent.mapper.CommentMapper;
import ru.notivent.model.Comment;
import ru.notivent.model.GeoPoint;
import ru.notivent.model.User;

@Service
@RequiredArgsConstructor
public class CommentService {

  @Delegate final CommentDao commentDao;

  final CommentMapper commentMapper;

  public ResponseEntity<List<CommentDto>> getComments(UUID geoPointUuid) {
    var comments = findAllByGeoPoint(geoPointUuid);
    var cmp = comparing(Comment::getCreatedAt);
    comments.sort(cmp);
    return ResponseEntity.ok(comments.stream().map(commentMapper::toDto).toList());
  }

  public ResponseEntity<Void> create(UUID userUuid, CommentPostDto dto) {
    var comment =
        Comment.builder()
            .user(User.builder().uuid(userUuid).build())
            .geoPoint(GeoPoint.builder().uuid(dto.getGeoPointUuid()).build())
            .text(dto.getText())
            .build();
    create(comment);
    return ResponseEntity.ok().build();
  }
}
