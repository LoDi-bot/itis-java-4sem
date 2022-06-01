package ru.itis.stogram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.stogram.models.Post;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private String description;
    private Long authorId;
    private String authorNick;
    private Long thumbnailId;
    private Long videoId;
    private Instant createdAt;
    private Integer likesCount;

    public static PostDto from(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .description(post.getDescription())
                .authorId(post.getAuthor().getId())
                .authorNick(post.getAuthor().getNick())
                .thumbnailId(post.getThumbnail().getId())
                .videoId(post.getVideo().getId())
                .createdAt(post.getCreatedAt())
                .likesCount(post.getWhoLiked().size())
                .build();
    }
    public static List<PostDto> from(List<Post> posts) {
        return posts.stream().map(PostDto::from).collect(Collectors.toList());
    }
}
