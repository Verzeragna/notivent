package ru.notivent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    UUID uuid;
    Tariff tariff;
    UUID userUuid;
    OffsetDateTime endAt;
}
