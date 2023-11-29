package ru.notivent.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends NotiventException {

    public UserNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
