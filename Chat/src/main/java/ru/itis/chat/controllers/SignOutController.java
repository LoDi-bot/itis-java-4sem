package ru.itis.chat.controllers;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/signOut")
public class SignOutController {

    @PostMapping
    public void sigOut(HttpSession session) {
        session.invalidate();
    }

}
