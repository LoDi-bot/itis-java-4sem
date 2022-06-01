package ru.itis.stogram.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class VideoLengthException extends RuntimeException {
    public VideoLengthException(String message) {
        super(message);
    }
}
