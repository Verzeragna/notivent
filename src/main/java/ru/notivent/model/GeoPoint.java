package ru.notivent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import ru.notivent.enums.GeoPointType;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoPoint {
    UUID uuid;
    UUID userUuid;
    GeoPointType type;
    double latitude;
    double longitude;
    double userLatitude;
    double userLongitude;
    String name;
    String description;
    OffsetDateTime createdAt;
    OffsetDateTime live;
    Integer grade;
    Location location;
}
