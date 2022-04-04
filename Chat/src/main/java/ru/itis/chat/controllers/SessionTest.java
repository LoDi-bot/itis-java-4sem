package ru.itis.chat.controllers;

import org.springframework.web.bind.annotation.*;
import ru.itis.chat.dto.UserDto;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/session")
public class SessionTest {

    @GetMapping
    public List<UserDto> getUsers(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<UserDto> users = (List<UserDto>) session.getAttribute("users");
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    @PostMapping("/persistUser")
    public List<UserDto> addUser(HttpSession session, @RequestBody UserDto userDto) {
        @SuppressWarnings("unchecked")
        List<UserDto> users = (List<UserDto>) session.getAttribute("users");
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(userDto);
        session.setAttribute("users", users);
        return users;
    }

    @PostMapping("/destroy")
    public String destroySession(HttpSession session) {
        session.invalidate();
        return "redirect:/session";
    }
}
