package src.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import src.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public Page<Comment> findByPostId(Long postId, Pageable page);
}
