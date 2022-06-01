package ru.itis.stogram.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class SubscribeToMyselfException extends RuntimeException {
    public SubscribeToMyselfException(String message) {
        super(message);
    }
}
