package ru.itis.stogram.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.stogram.dto.PostDto;
import ru.itis.stogram.dto.PostsPage;
import ru.itis.stogram.exceptions.AccessToForeignDataException;
import ru.itis.stogram.exceptions.PostNotFoundException;
import ru.itis.stogram.models.Account;
import ru.itis.stogram.models.FileInfo;
import ru.itis.stogram.models.Post;
import ru.itis.stogram.repositories.AccountsRepository;
import ru.itis.stogram.repositories.PostsRepository;
import ru.itis.stogram.services.FilesService;
import ru.itis.stogram.services.PostService;
import ru.itis.stogram.services.SubscriptionService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private final PostsRepository postsRepository;
    private final AccountsRepository accountsRepository;
    private final SubscriptionService subscriptionService;

    private final FilesService filesService;

    @Value("${posts.default-page-size}")
    private int defaultPageSize;


    @Transactional
    @Override
    public PostDto addNewPost(MultipartFile multipartFile, String description, Long authorId) {
        FileInfo video = filesService.upload(multipartFile);
        FileInfo thumbnail = filesService.uploadThumbnailFromVideo(video);

        Post post = Post.builder()
                .video(video)
                .thumbnail(thumbnail)
                .description(description)
                .author(accountsRepository.getById(authorId))
                .whoLiked(new HashSet<>())
                .build();

        post = postsRepository.save(post);

        return PostDto.from(post);
    }

    @Override
    public PostDto getPostById(Long postId) {
        Post post = postsRepository
                .findById(postId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new PostNotFoundException(String.format("Post with id %s not found", postId)));

        return PostDto.from(post);
    }

    @Override
    public PostsPage getPosts(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Post> postsPage = postsRepository.findAll(pageRequest);

        return PostsPage.builder()
                .posts(PostDto.from(postsPage.getContent()))
                .totalPages(postsPage.getTotalPages())
                .currentPage(postsPage.getNumber())
                .build();
    }

    @Override
    public PostsPage getPostsBySubscriptions(Integer page, Long subscriberId) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Post> postsPage = postsRepository.findAllByAuthorIn(pageRequest, subscriptionService.getSubscriptions(subscriberId));

        return PostsPage.builder()
                .posts(PostDto.from(postsPage.getContent()))
                .totalPages(postsPage.getTotalPages())
                .currentPage(postsPage.getNumber())
                .build();
    }

    @Override
    public PostsPage getPostsByAuthorId(Integer page, Long authorId) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize);
        Page<Post> postsPage = postsRepository.findAllByAuthorId(pageRequest, authorId);

        return PostsPage.builder()
                .posts(PostDto.from(postsPage.getContent()))
                .totalPages(postsPage.getTotalPages())
                .currentPage(postsPage.getNumber())
                .build();
    }

    @Override
    public void deletePostById(Long postId, Long accountId) {
        Post post = postsRepository
                .findById(postId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new PostNotFoundException(String.format("Post with id %s not found", postId)));

        if (!Objects.equals(post.getAuthor().getId(), accountId)) {
            throw new AccessToForeignDataException("You don't have access to change another users data");
        }

        postsRepository.delete(post);
    }

    @Override
    public PostDto editDescriptionById(Long postId, String newDescription, Long accountId) {
        Post post = postsRepository
                .findById(postId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new PostNotFoundException(String.format("Post with id %s not found", postId)));

        if (!Objects.equals(post.getAuthor().getId(), accountId)) {
            throw new AccessToForeignDataException("You don't have access to change another users data");
        }

        post.setDescription(newDescription);

        return PostDto.from(postsRepository.save(post));
    }

    @Override
    public PostDto putLikeOnPost(Long postId, Long accountId) {
        Post post = postsRepository
                .findById(postId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new PostNotFoundException(String.format("Post with id %s not found", postId)));

        if (post.getWhoLiked().contains(accountsRepository.getById(accountId))) {
            return PostDto.from(post);
        } else {
            post.getWhoLiked().add(accountsRepository.getById(accountId));
            return PostDto.from(postsRepository.save(post));
        }
    }

    @Override
    public PostDto removeLikeFromPost(Long postId, Long accountId) {
        Post post = postsRepository
                .findById(postId)
                .orElseThrow((Supplier<RuntimeException>)
                        () -> new PostNotFoundException(String.format("Post with id %s not found", postId)));

        if (post.getWhoLiked().contains(accountsRepository.getById(accountId))) {
            post.getWhoLiked().remove(accountsRepository.getById(accountId));
            return PostDto.from(postsRepository.save(post));
        } else {
            return PostDto.from(post);
        }
    }
}
