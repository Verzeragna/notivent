package ru.notivent.exception;

import org.springframework.http.HttpStatus;

public class DistanceAcceptableException extends NotiventException {
    public DistanceAcceptableException(HttpStatus status) {
        super(status);
    }
}
