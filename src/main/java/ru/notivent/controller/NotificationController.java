package ru.notivent.controller;

import static ru.notivent.util.HttpUtil.X_UUID;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.notivent.service.NotificationService;

@RestController
@RequestMapping("notification")
@AllArgsConstructor
public class NotificationController {

    final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<String> getNotification(@RequestHeader(X_UUID) UUID userUuid) {
        return notificationService.getNotification(userUuid);
    }
}
