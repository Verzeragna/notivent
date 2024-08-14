package ru.notivent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.NotifyStatus;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    UUID uuid;
    String message;
    NotifyStatus status;
    OffsetDateTime createdAt;
    OffsetDateTime endAt;
}
