package ru.itis.stogram.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.stogram.services.ConfirmationService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/confirmation")
public class ConfirmationController {

    private final ConfirmationService confirmationService;

    @GetMapping("/{confirmation-code}")
    public String confirm(@PathVariable("confirmation-code") String confirmationCode, Model model) {
        if (confirmationService.confirm(confirmationCode)) {
            model.addAttribute("text", "Аккаунт успешно активирован!");
        } else {
            model.addAttribute("text", "Ссылка недействительна!");
        }
        return "confirmation_status";
    }

}
