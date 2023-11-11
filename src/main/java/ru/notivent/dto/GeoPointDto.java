package ru.notivent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.GeoPointType;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoPointDto {
    long id;
    String userId;
    GeoPointType type;
    double latitude;
    double longitude;
    String name;
    String description;
    Instant createdAt;
}
