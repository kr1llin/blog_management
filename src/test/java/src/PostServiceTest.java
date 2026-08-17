package src;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import src.exceptions.ResourceNotFoundException;
import src.model.Post;
import src.model.PostStatus;
import src.model.User;
import src.model.dto.PostRequest;
import src.model.dto.PostResponse;
import src.repositories.PostRepository;
import src.repositories.TagRepository;
import src.repositories.UserRepository;
import src.model.dto.PostMapper;
import src.services.PostService;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostService postService;

    private User author;
    private Post post;
    private PostRequest request;
    private PostResponse response;

    @BeforeEach
    void init() {
        author = new User();
        author.setId(1L);
        author.setUsername("bob");

        request = new PostRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        request.setTagNames(Set.of("java", "spring"));

        post = new Post();
        post.setId(10L);
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setStatus(PostStatus.DRAFT);
        post.setAuthor(author);

        response = PostResponse.builder()
                .id(10L)
                .title("Test Title")
                .content("Test Content")
                .status("DRAFT")
                .authorUsername("bob")
                .build();
    }

    @Test
    void createPost_success() {
        Mockito.when(userRepository.findByUsername("bob")).thenReturn(Optional.of(author));
        Mockito.when(postMapper.toEntity(request)).thenReturn(post);
        Mockito.when(postRepository.save(ArgumentMatchers.any(Post.class))).thenReturn(post);
        Mockito.when(postMapper.toResponse(post)).thenReturn(response);

        PostResponse result = postService.createPost(request, "bob");

        assertThat(result).isNotNull();
        Assertions.assertThat(result.getTitle()).isEqualTo("Test Title");
        Assertions.assertThat(result.getAuthorUsername()).isEqualTo("bob");
        Mockito.verify(postRepository, Mockito.times(1)).save(ArgumentMatchers.any(Post.class));
        Mockito.verify(postMapper, Mockito.times(1)).toEntity(request);
    }

    @Test
    void createPost_userNotFound_throwsException() {
        Mockito.when(userRepository.findByUsername("foo")).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.createPost(request, "foo")
        );
    }
}