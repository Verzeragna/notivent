package ru.notivent.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.dao.CommentPostDto;
import ru.notivent.dto.CommentDto;
import ru.notivent.service.CommentService;

import static ru.notivent.util.HttpUtil.X_AUTH;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestHeader(X_AUTH) UUID userUuid, @Valid @RequestBody CommentPostDto dto) {
        return commentService.create(userUuid, dto);
    }

    @GetMapping("point/{id}")
    public ResponseEntity<List<CommentDto>> getCommentsByGeoPoint(@PathVariable("id") UUID uuid) {
        return commentService.getComments(uuid);
    }

    @GetMapping("point/{id}/count")
    public ResponseEntity<Integer> getGeoPointCommentsCount(@PathVariable("id") UUID uuid) {
        return commentService.getGeoPointCommentsCount(uuid);
    }
}
