package src.repositories;

import org.springframework.data.repository.CrudRepository;
import src.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
