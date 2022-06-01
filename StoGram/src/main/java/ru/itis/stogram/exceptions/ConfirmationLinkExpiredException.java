package ru.itis.stogram.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@ResponseStatus(value = HttpStatus.MOVED_PERMANENTLY)
public class ConfirmationLinkExpiredException extends RuntimeException{
    public ConfirmationLinkExpiredException(final String message){
        super(message);
    }
}
