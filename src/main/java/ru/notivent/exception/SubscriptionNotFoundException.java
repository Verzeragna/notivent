package ru.notivent.exception;

import org.springframework.http.HttpStatus;

public class SubscriptionNotFoundException extends NotiventException {
    public SubscriptionNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
