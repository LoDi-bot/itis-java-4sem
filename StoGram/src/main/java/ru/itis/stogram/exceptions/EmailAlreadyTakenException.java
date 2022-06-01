package ru.itis.stogram.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
public class EmailAlreadyTakenException extends RuntimeException{
    public EmailAlreadyTakenException(final String message){
        super(message);
    }
}
