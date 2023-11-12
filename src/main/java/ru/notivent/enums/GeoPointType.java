package ru.notivent.enums;

public enum GeoPointType {
    PRIVATE("Личная"),
    PUBLIC("Публичная");

    public final String value;


    GeoPointType(String value) {
        this.value = value;
    }
}
