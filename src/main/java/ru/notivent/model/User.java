package ru.notivent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    UUID uuid;
    String login;
    String password;
    Instant createdAt;
    String userName;
}
