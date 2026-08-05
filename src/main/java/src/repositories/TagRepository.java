package src.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import src.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
