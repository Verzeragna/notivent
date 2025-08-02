package ru.notivent.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    UUID uuid;
    String nickName;
    String profileImage;
    String text;
    Instant createdAt;
}
