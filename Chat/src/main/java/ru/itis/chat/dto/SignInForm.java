package ru.itis.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInForm {
    @Email(message = "INVALID_EMAIL")
    private String email;
    @Length(min = 4, message = "PASSWORD_TOO_SHORT")
    @Length(max = 24, message = "PASSWORD_TOO_LONG")
    private String password;
}
