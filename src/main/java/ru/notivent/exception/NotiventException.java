package ru.notivent.exception;

import org.springframework.http.HttpStatus;

public class NotiventException extends RuntimeException {
    private HttpStatus status;

    public NotiventException(HttpStatus status) {
        this.status = status;
    }
}
