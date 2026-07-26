package src.repositories;

import org.springframework.data.repository.CrudRepository;
import src.model.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
