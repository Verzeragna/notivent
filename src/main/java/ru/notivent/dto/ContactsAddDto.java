package ru.notivent.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ContactsAddDto(@NotNull UUID mainUserId, @NotNull UUID subUserId, @NotNull Instant createdAt) {}
