package ru.itis.stogram.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.stogram.dto.SignUpForm;
import ru.itis.stogram.services.SignUpService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/signUp")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpForm form) {
        signUpService.signUp(form);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("{}");
    }

}
