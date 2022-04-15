package ru.itis.chat.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorEntity {
    // Общие ошибки
    INVALID_REQUEST(400, "Invalid request"),
    INVALID_TOKEN(403, "Token authorization error"),
    NOT_FOUND(404, "Not found"),
    EMAIL_ALREADY_TAKEN(400, "Email already taken"),
    ACCESS_TO_FOREIGN_DATA(403, "Denied access to foreign users data"),

    //Registration
    FIRST_NAME_TOO_SHORT(400, "First name should contain at least 2 characters"),
    FIRST_NAME_TOO_LONG(400, "First name can't contain more than 16 characters"),
    LAST_NAME_TOO_SHORT(400, "Last name should contain at least 2 characters"),
    LAST_NAME_TOO_LONG(400, "Last name can't contain more than 16 characters"),
    INVALID_EMAIL(400, "Invalid Email"),
    PASSWORD_TOO_SHORT(400, "Password should contain at least 4 characters"),
    PASSWORD_TOO_LONG(400, "Password can't contain more than 24 characters"),

    //Login
    ACCOUNT_NOT_FOUND(404,"Account not found"),
    INCORRECT_PASSWORD(400, "Incorrect password"),

    //Chats
    CHAT_NOT_FOUND(404, "Chat not found"),

    //Messages
    MESSAGE_NOT_FOUND(404, "Message not found"),
    MESSAGE_ID_EMPTY(400, "Message id can't be null"),
    BLANK_MESSAGE_BODY(400, "Message text body can't be blank"),
    MESSAGE_AUTHOR_EMPTY(400, "Message should have author"),
    MESSAGE_CHAT_EMPTY(400, "Message should have chat");

    int status;
    String message;

    @JsonIgnore
    String validatorKey;
    @JsonIgnore
    Logger log = LoggerFactory.logger(ErrorEntity.class);

    ErrorEntity(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public void log() {
        log.error("Ошибка " + status + ": " + message);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String rawValue() {
        return toString();
    }
}
