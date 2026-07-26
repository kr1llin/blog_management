package src.repositories;

import org.springframework.data.repository.CrudRepository;
import src.model.Post;

public interface PostRepository extends CrudRepository<Post, Long> {
}
