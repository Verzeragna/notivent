package ru.notivent.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
}
