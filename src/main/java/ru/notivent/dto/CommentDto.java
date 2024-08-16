package ru.notivent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    UUID uuid;
    String nickName;
    String text;
    OffsetDateTime createdAt;
}
