package ru.notivent.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
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
}
