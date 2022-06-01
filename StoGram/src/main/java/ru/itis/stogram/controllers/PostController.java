package ru.itis.stogram.controllers;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.stogram.dto.PostDto;
import ru.itis.stogram.dto.PostsPage;
import ru.itis.stogram.services.FilesService;
import ru.itis.stogram.services.PostService;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final FilesService filesService;

    @PostMapping("/new")
    public ResponseEntity<PostDto> addNewPost(@RequestParam("file") MultipartFile multipartFile,
                                              @RequestParam("description") String description,
                                              Authentication authentication) {
        Long authorId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.addNewPost(multipartFile, description, authorId));
    }

    @GetMapping("/{post-id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("post-id") Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @GetMapping(value = "/all", params = {"page"})
    public ResponseEntity<PostsPage> getPosts(@RequestParam("page") Integer page) {
        return ResponseEntity.ok(postService.getPosts(page));
    }

    @GetMapping(value = "/all/subscriptions", params = {"page"})
    public ResponseEntity<PostsPage> getPosts(@RequestParam("page") Integer page,
                                              Authentication authentication) {
        Long subscriberId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());

        return ResponseEntity.ok(postService.getPostsBySubscriptions(page, subscriberId));
    }

    @GetMapping(value = "/all", params = {"page", "authorId"})
    public ResponseEntity<PostsPage> getPostsByAuthorId(@RequestParam("page") Integer page,
                                                        @RequestParam("authorId") Long authorId) {
        return ResponseEntity.ok(postService.getPostsByAuthorId(page, authorId));
    }

    @GetMapping("/{post-id}/video")
    public void getVideoByPostId(@PathVariable("post-id") Long postId,
                                 HttpServletResponse response) {
        filesService.addFileToResponse(postService.getPostById(postId).getVideoId(), response);
    }

    @GetMapping("/{post-id}/thumbnail")
    public void getThumbnailByPostId(@PathVariable("post-id") Long postId,
                                     HttpServletResponse response) {
        filesService.addFileToResponse(postService.getPostById(postId).getThumbnailId(), response);
    }

    @DeleteMapping("/{post-id}/delete")
    public void deletePostById(@PathVariable("post-id") Long postId,
                               Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());

        postService.deletePostById(postId, accountId);
    }

    @PutMapping("/{post-id}/edit")
    public ResponseEntity<PostDto> editPostDescriptionById(@PathVariable("post-id") Long postId,
                                                           @RequestParam("new-description") String newDescription,
                                                           Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());

        return ResponseEntity.ok(postService.editDescriptionById(postId, newDescription, accountId));
    }

    @PostMapping("/{post-id}/putLike")
    public ResponseEntity<Integer> putLikeOnPost(@PathVariable("post-id") Long postId,
                                                 Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());

        return ResponseEntity.ok(postService.putLikeOnPost(postId, accountId).getLikesCount());
    }

    @PostMapping("/{post-id}/removeLike")
    public ResponseEntity<Integer> removeLikeFromPost(@PathVariable("post-id") Long postId,
                                                      Authentication authentication) {
        Long accountId = Long.valueOf(JWT.decode(authentication.getPrincipal().toString()).getSubject());

        return ResponseEntity.ok(postService.removeLikeFromPost(postId, accountId).getLikesCount());
    }

}
