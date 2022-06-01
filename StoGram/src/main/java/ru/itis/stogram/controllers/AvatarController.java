package ru.itis.stogram.controllers;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.stogram.dto.FileInfoDto;
import ru.itis.stogram.services.FilesService;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final FilesService filesService;

    @PostMapping("/upload")
    public ResponseEntity<FileInfoDto> uploadAndSetAvatar(@RequestParam("file") MultipartFile multipartFile,
                                                          Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(filesService.uploadAndSetAvatar(multipartFile, accountId));
    }

    @GetMapping("/{avatar-id}")
    public void getAvatarById(@PathVariable("avatar-id") Long avatarId, HttpServletResponse response) {
        filesService.addFileToResponse(avatarId, response);
    }

}
