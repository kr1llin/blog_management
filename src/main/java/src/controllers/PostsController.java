package src.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import src.configs.properties.PostProperties;
import src.model.dto.PostRequest;
import src.model.dto.PostResponse;
import src.repositories.PostRepository;
import src.services.PostService;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostsController {
    PostService postService;
    PostProperties postProps;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request, Authentication authentication){
        PostResponse response = postService.createPost(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    };
}
