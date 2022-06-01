package ru.itis.stogram.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessToForeignDataException extends RuntimeException{
    public AccessToForeignDataException(final String message) {
        super(message);
    }
}
