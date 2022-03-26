package ru.itis.chat.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.chat.dto.SignUpForm;
import ru.itis.chat.services.SignUpService;

@RequiredArgsConstructor
@Controller
@RequestMapping("sign-up")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping
    public String signUp(SignUpForm signUpForm) {
        signUpService.signUp(signUpForm);
        return "redirect:/sign-in";
    }

}
