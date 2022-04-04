package ru.itis.chat.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.chat.dto.SignUpForm;
import ru.itis.chat.services.SignUpService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/signUp")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping
    public void signUp(@RequestBody SignUpForm signUpForm) {
        signUpService.signUp(signUpForm);
    }

}
