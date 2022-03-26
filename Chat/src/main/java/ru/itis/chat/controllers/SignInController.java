package ru.itis.chat.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.chat.dto.SignInForm;
import ru.itis.chat.services.SignInService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/sign-in")
public class SignInController {

    private final SignInService signInService;

    @PostMapping
    public String signIn(SignInForm signInForm) {
        signInService.signIn(signInForm);
        return "redirect:/chats";
    }
}
