package ru.itis.stogram.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.stogram.dto.FileInfoDto;
import ru.itis.stogram.exceptions.AccountNotFoundException;
import ru.itis.stogram.exceptions.FileNotFoundException;
import ru.itis.stogram.exceptions.VideoLengthException;
import ru.itis.stogram.models.Account;
import ru.itis.stogram.models.FileInfo;
import ru.itis.stogram.repositories.AccountsRepository;
import ru.itis.stogram.repositories.FileInfoRepository;
import ru.itis.stogram.services.FilesService;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class FilesServiceImpl implements FilesService {
    private final FileInfoRepository fileInfoRepository;
    private final AccountsRepository accountsRepository;

    @Value("${files.storage.path}")
    private String storagePath;

    @Transactional
    @Override
    public FileInfoDto uploadAndSetAvatar(MultipartFile multipartFile, Long accountId) {
        FileInfo fileInfo = this.upload(multipartFile);
        Account account = accountsRepository
                .findById(accountId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new AccountNotFoundException(String.format("Account with id %s not found", accountId)));

        account.setAvatar(fileInfo);
        accountsRepository.save(account);

        return FileInfoDto.builder()
                .id(fileInfo.getId())
                .size(fileInfo.getSize())
                .type(fileInfo.getType())
                .build();
    }

    @Transactional
    @Override
    public FileInfo upload(MultipartFile multipartFile) {
        try {
            String fileExtension = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf("."));
            String storageFileName = UUID.randomUUID().toString().concat(fileExtension);

            if (Objects.requireNonNull(multipartFile.getContentType()).startsWith("video/") && !checkVideoDuration(multipartFile.getInputStream())) {
                throw new VideoLengthException("Видео не может быть короче 3 или длиннее 20 секунд :(");
            }

            FileInfo fileInfo = FileInfo.builder()
                    .originalFileName(multipartFile.getOriginalFilename())
                    .storageFileName(storageFileName)
                    .size(multipartFile.getSize())
                    .type(multipartFile.getContentType())
                    .build();

            fileInfoRepository.save(fileInfo);

            Files.copy(multipartFile.getInputStream(), Paths.get(storagePath, fileInfo.getStorageFileName()));

            return fileInfo;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void addFileToResponse(Long fileId, HttpServletResponse response) {
        FileInfo fileInfo;

        if (fileId == 0) {
            fileInfo = FileInfo.builder()
                    .originalFileName("avatar.jpeg")
                    .storageFileName("avatar.jpeg")
                    .size(5888L)
                    .type("image/jpeg")
                    .build();
        } else {
            fileInfo = fileInfoRepository
                    .findById(fileId)
                    .orElseThrow(FileNotFoundException::new);
        }

        response.setContentLength(fileInfo.getSize().intValue());
        response.setContentType(fileInfo.getType());
        response.setHeader("Content-Disposition", "filename=\"" + fileInfo.getOriginalFileName() + "\"");

        try {
            IOUtils.copy(new FileInputStream(storagePath + "/" + fileInfo.getStorageFileName()), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    @Transactional
    @Override
    public FileInfo uploadThumbnailFromVideo(FileInfo video) {
        try {
            Picture frame = FrameGrab.getFrameFromFile(new File(storagePath + "/" + video.getStorageFileName()), 40);
            String storageFileName = UUID.randomUUID().toString().concat(".png");
            FileInfo fileInfo = FileInfo.builder()
                    .storageFileName(storageFileName)
                    .size((long) frame.getHeight() * frame.getWidth() * frame.getColor().bitsPerPixel / 8)
                    .type("image/png")
                    .build();
            fileInfoRepository.save(fileInfo);
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(frame);
            ImageIO.write(bufferedImage, "png", new File(storagePath + "/" + storageFileName));
            return fileInfo;
        } catch (IOException | JCodecException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean checkVideoDuration(InputStream inputStream) {
        long duration = 0L;
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputStream);
        try {
            frameGrabber.start();
            duration = frameGrabber.getLengthInTime() / (1000 * 1000);
            frameGrabber.stop();
            return duration >= 3 && duration <= 20;
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
