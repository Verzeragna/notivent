package ru.notivent.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.GeoPointType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoPointDto implements Serializable {
    @NotNull
    UUID uuid;
    @NotNull
    GeoPointType type;
    double latitude;
    double longitude;
    Instant live;
    double userLatitude;
    double userLongitude;
    @NotNull
    String name;
    @NotNull
    String description;
    @NotNull
    Instant createdAt;
    @NotNull
    Integer grade;
    @NotNull
    LocationDto location;
    @NotNull
    Map<String,String> images;
}
