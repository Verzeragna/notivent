package ru.notivent.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.GeoPointType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoPointHistory {
    UUID uuid;
    UUID userUuid;
    GeoPointType type;
    double latitude;
    double longitude;
    double userLatitude;
    double userLongitude;
    String name;
    String description;
    Instant createdAt;
    Instant live;
    Integer grade;
    Location location;
}
