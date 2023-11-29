package ru.notivent.exception;

import org.springframework.http.HttpStatus;

public class SubscriptionException extends NotiventException {
    public SubscriptionException(HttpStatus status) {
        super(status);
    }

    public SubscriptionException(String message, HttpStatus status) {
        super(message, status);
    }
}
