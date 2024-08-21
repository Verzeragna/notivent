package ru.notivent.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.NotifyStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    UUID uuid;
    String message;
    NotifyStatus status;
    Instant createdAt;
    Instant endAt;
}
