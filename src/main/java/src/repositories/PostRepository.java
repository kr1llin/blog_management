package src.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import src.model.Post;
import src.model.PostStatus;
import src.model.Tag;

public interface PostRepository extends JpaRepository<Post, Long>{
    Page<Post> findByTag(String tag, Pageable page);
    Page<Post> findByStatus(PostStatus status, Pageable page);
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable page);
}
