package src.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import src.exceptions.UserNotFoundException;
import src.model.Post;
import src.model.PostStatus;
import src.model.Tag;
import src.model.User;
import src.model.dto.PostMapper;
import src.model.dto.PostRequest;
import src.model.dto.PostResponse;
import src.repositories.PostRepository;
import src.repositories.TagRepository;
import src.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class PostService {
    PostRepository postRepo;
    UserRepository userRepo;
    TagRepository tagRepo;
    PostMapper postMapper;

    public PostResponse createPost(PostRequest postRequest, String username){
        User author = userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        Post post = postMapper.toEntity(postRequest);

        post.setAuthor(author);
        post.setStatus(PostStatus.DRAFT);

        Set<Tag> tags = resolveTags(postRequest.getTagNames());
        post.setTags(tags);
        Post saved = postRepo.save(post);
        return postMapper.toResponse(saved);
    }

    private Set<Tag> resolveTags(Set<String> strTags) {
        if (strTags == null) return new HashSet<>();
        return strTags.stream().map(tag -> tagRepo.findByName(tag).
                orElseGet(() -> tagRepo.save(new Tag(tag)))).collect(Collectors.toSet());
    }
}
