package ru.itis.stogram.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.stogram.dto.PostDto;
import ru.itis.stogram.dto.PostsPage;

public interface PostService {
    PostDto addNewPost(MultipartFile multipartFile, String description, Long authorId);

    PostDto getPostById(Long postId);

    PostsPage getPosts(Integer page);

    PostsPage getPostsBySubscriptions(Integer page, Long subscriberId);

    PostsPage getPostsByAuthorId(Integer page, Long authorId);

    void deletePostById(Long postId, Long accountId);

    PostDto editDescriptionById(Long postId, String newDescription, Long accountId);

    PostDto putLikeOnPost(Long postId, Long accountId);

    PostDto removeLikeFromPost(Long postId, Long accountId);
}
