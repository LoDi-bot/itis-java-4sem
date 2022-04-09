package ru.itis.chat.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.chat.dto.SignUpForm;
import ru.itis.chat.services.SignUpService;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
@RequestMapping("/signUp")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody SignUpForm signUpForm, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            signUpService.signUp(signUpForm);
        }
    }

}
