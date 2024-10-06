package ru.notivent.dto;

import jakarta.validation.constraints.NotNull;

public record SearchContactDto(@NotNull String value) {}
