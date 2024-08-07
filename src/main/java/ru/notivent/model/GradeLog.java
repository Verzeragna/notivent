package ru.notivent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.GradeType;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeLog {
    UUID uuid;
    UUID userUuid;
    GradeType gradeType;
    UUID geoPointUuid;
    OffsetDateTime date;
}
