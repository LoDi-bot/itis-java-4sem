package ru.itis.stogram.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "file_infos")
public class FileInfo extends AbstractEntity {
    @Column(name = "original_file_name", nullable = true)
    private String originalFileName;

    @Column(name = "storage_file_name", unique = true, nullable = false)
    private String storageFileName;

    private Long size;

    private String type;
}
