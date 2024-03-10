package ru.notivent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    UUID uuid;
    User user;
    GeoPoint geoPoint;
    String text;
    Instant createdAt;
}
