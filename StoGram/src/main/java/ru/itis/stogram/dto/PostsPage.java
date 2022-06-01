package ru.itis.stogram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostsPage {
    private List<PostDto> posts;
    private Integer totalPages;
    private Integer currentPage;
}
