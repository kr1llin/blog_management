package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import src.model.Post;
import src.model.PostStatus;
import src.model.User;
import src.model.Tag;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
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
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(author));
        when(postMapper.toEntity(request)).thenReturn(post);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.toResponse(post)).thenReturn(response);

        PostResponse result = postService.createPost(request, "bob");

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getAuthorUsername()).isEqualTo("bob");
        verify(postRepository, times(1)).save(any(Post.class));
        verify(postMapper, times(1)).toEntity(request);
    }

    @Test
    void createPost_userNotFound_throwsException() {
        when(userRepository.findByUsername("foo")).thenReturn(Optional.empty());

        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> postService.createPost(request, "foo")
        );
    }
}