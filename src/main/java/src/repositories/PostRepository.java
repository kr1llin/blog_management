package src.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import src.model.Post;
import src.model.PostStatus;
import src.model.Tag;

public interface PostRepository extends CrudRepository<Post, Long> {
    Page<Post> getByTag(Tag tag, Pageable page);
    Page<Post> getByStatus(PostStatus status, Pageable page);
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable page);
}
