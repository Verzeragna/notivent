package ru.notivent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.GeoPointType;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoPoint {
    long id;
    String userId;
    GeoPointType type;
    double latitude;
    double longitude;
    String name;
    String description;
    Instant createdAt;
    Instant live;
}
