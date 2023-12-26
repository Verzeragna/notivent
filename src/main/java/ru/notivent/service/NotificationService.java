package ru.notivent.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.notivent.dao.NotificationDao;
import ru.notivent.dto.NotificationDto;
import ru.notivent.mapper.NotificationMapper;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Delegate
    final NotificationDao notificationDao;
    final UserService userService;
    final NotificationMapper notificationMapper;

    public ResponseEntity<String> getNotification(UUID userUuid) {
        val user = userService.findById(userUuid);
        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return findLastActive().map(value -> ResponseEntity.ok(value.getMessage())).orElseGet(() -> ResponseEntity.ok().build());
    }
}
