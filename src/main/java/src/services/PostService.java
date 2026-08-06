package src.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.exceptions.ResourceNotFoundException;
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
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class PostService {
    PostRepository postRepo;
    UserRepository userRepo;
    TagRepository tagRepo;
    protected PostMapper postMapper;

    @Transactional
    public PostResponse createPost(PostRequest postRequest, String username){
        User author = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(username));
        Post post = postMapper.toEntity(postRequest);

        post.setAuthor(author);
        post.setStatus(PostStatus.DRAFT);

        Set<Tag> tags = resolveTags(postRequest.getTagNames());
        post.setTags(tags);
        Post saved = postRepo.save(post);
        return postMapper.toResponse(saved);
    }

    @Transactional
    public PostResponse patchPost(Long id, PostRequest postRequest){
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Post %s not found", id)));
        if (postRequest.getTitle() != null) post.setTitle(postRequest.getTitle());
        if (postRequest.getContent() != null) post.setContent(postRequest.getContent());
        if (postRequest.getTagNames() != null) post.setTags(resolveTags(postRequest.getTagNames()));
        return postMapper.toResponse(post);
    }

    public Page<PostResponse> getAllPublishedPosts(Pageable page){
        Page<Post> posts = postRepo.findByStatus(PostStatus.PUBLISHED, page);
        return posts.map(post -> postMapper.toResponse(post));
    }

    public Page<PostResponse> getFilteredPosts(String title, String tag, Pageable pageable) {
        if (title != null && !title.isBlank()) {
            return postRepo.findByTitleContainingIgnoreCase(title, pageable)
                    .map(postMapper::toResponse);
        }
        if (tag != null && !tag.isBlank()) {
                return postRepo.findByTag(tag, pageable)
                    .map(postMapper::toResponse);
        }
        return postRepo.findByStatus(PostStatus.PUBLISHED, pageable)
                .map(postMapper::toResponse);
    }

    public void deletePost(Long id){
        postRepo.deleteById(id);
    }

    private Set<Tag> resolveTags(Set<String> strTags) {
        if (strTags == null) return new HashSet<>();
        return strTags.stream().map(tag -> tagRepo.findByName(tag).
                orElseGet(() -> tagRepo.save(new Tag(tag)))).collect(Collectors.toSet());
    }
}
