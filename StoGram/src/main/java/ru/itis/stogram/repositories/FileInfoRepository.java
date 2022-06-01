package ru.itis.stogram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.stogram.models.FileInfo;

public interface FileInfoRepository  extends JpaRepository<FileInfo, Long> {
}
