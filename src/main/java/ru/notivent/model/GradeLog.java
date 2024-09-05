package ru.notivent.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.notivent.enums.GradeType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeLog {
    UUID uuid;
    UUID userUuid;
    GradeType gradeType;
    UUID geoPointUuid;
    Instant date;
}
