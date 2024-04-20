package ru.notivent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.GeoPointType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoPointDto {
    UUID uuid;
    GeoPointType type;
    double latitude;
    double longitude;
    Instant live;
    double userLatitude;
    double userLongitude;
    String name;
    String description;
    Instant createdAt;
    Integer grade;
    LocationDto location;
}
