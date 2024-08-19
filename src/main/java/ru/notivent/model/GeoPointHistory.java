package ru.notivent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.GeoPointType;

import java.time.OffsetDateTime;
import java.util.UUID;

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
    OffsetDateTime createdAt;
    OffsetDateTime live;
    Integer grade;
    Location location;
}
