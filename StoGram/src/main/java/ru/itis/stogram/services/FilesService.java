package ru.itis.stogram.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.stogram.dto.FileInfoDto;
import ru.itis.stogram.models.FileInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

public interface FilesService {
    FileInfoDto uploadAndSetAvatar(MultipartFile multipartFile, Long accountId);

    FileInfo upload(MultipartFile multipartFile);

    void addFileToResponse(Long fileId, HttpServletResponse response);

    FileInfo uploadThumbnailFromVideo(FileInfo fileInfo);

    boolean checkVideoDuration(InputStream inputStream);
}
