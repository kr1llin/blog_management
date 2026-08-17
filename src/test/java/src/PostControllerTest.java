package src;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import src.configs.properties.PostProperties;
import src.controllers.PostsController;
import src.model.dto.PostRequest;
import src.model.dto.PostResponse;
import src.proxies.JwtTokenProvider;
import src.services.PostService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(PostsController.class)
@Import(PostControllerTest.TestConfig.class)
public class PostControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    PostService postService;

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @TestConfiguration
    @EnableMethodSecurity
    static class TestConfig {
        @Bean
        PostProperties postProperties() {
            PostProperties props = new PostProperties();
            props.setPageSize(20);
            return props;
        }
    }

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

        Mockito.when(postService.createPost(ArgumentMatchers.any(PostRequest.class), ArgumentMatchers.eq("bob"))).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.post("/posts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("TEST TITLE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorUsername").value("bob"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void unauthorizedCreatePost_HttpUnauthorized() throws Exception {
        PostRequest request = new PostRequest();
        request.setContent("TEST CONTENT");
        request.setTitle("TEST TITLE");

        PostResponse response = PostResponse.builder()
                .id(1L)
                .title("TEST TITLE")
                .content("TEST CONTENT")
                .authorUsername("unknown")
                .status("DRAFT").build();

        Mockito.when(postService.createPost(ArgumentMatchers.any(PostRequest.class), ArgumentMatchers.eq("unknown"))).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.post("/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }
}
