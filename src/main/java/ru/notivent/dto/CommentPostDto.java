package ru.notivent.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentPostDto {
    @NotNull
    UUID geoPointUuid;
    @NotNull
    String text;
    @NotNull
    Instant createdAt;
}
