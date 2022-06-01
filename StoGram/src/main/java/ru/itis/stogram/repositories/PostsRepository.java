package ru.itis.stogram.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.stogram.models.Account;
import ru.itis.stogram.models.Post;

import java.util.List;

public interface PostsRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.author.id = ?1")
    Page<Post> findAllByAuthorId(Pageable pageable, Long authorId);

    @Query("select p from Post p where p.author in ?1")
    Page<Post> findAllByAuthorIn(Pageable pageable, List<Account> authors);

    List<Post> findAllByAuthorId(Long authorId);
}
