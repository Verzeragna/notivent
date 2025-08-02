package ru.notivent.dto;


import java.util.UUID;

public record ContactsDto(UUID id, String userName, String profileImage) {}
