package src.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import src.model.Post;
import src.repositories.PostRepository;

import java.util.Optional;

@Service("auth")
@AllArgsConstructor
public class AuthorizationService {
    final PostRepository postRepo;

    public boolean isPostAuthor(Long postId, String username){
        Optional<Post> postAuthor = postRepo.findById(postId);
        if (postAuthor.isEmpty()) return false;

        return postAuthor.get().getAuthor().getUsername().equals(username);
    }
}
