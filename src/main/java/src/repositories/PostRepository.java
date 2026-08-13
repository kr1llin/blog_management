package src.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import src.model.Post;
import src.model.PostStatus;

public interface PostRepository extends JpaRepository<Post, Long>{
    @Query(value = "SELECT * FROM posts WHERE tag in posts.tags", nativeQuery = true)
    Page<Post> findByTag(String tag, Pageable page);
    Page<Post> findByStatus(PostStatus status, Pageable page);
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable page);
}