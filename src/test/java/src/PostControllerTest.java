package src;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;
import src.BlogManagementApp;
import src.controllers.PostsController;
import src.model.Post;
import src.model.PostStatus;
import src.model.User;
import src.model.dto.PostRequest;
import src.model.dto.PostResponse;
import src.services.PostService;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostsController.class)
public class PostControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "bob", roles = {"USER"})
    void authorizedCreatePost_HttpCreated() throws Exception {
        PostRequest request = new PostRequest();
        request.setContent("TEST CONTENT");
        request.setTitle("TEST TITLE");

        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("TEST TITLE")
                .content("TEST CONTENT")
                .authorUsername("bob")
                .status("DRAFT").build();

        when(postService.createPost(any(PostRequest.class), eq("bob"))).thenReturn(response);

        mvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.authorUsername").value("bob"));
    }
}
