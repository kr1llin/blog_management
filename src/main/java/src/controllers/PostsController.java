package src.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import src.configs.properties.PostProperties;
import src.model.dto.PostRequest;
import src.model.dto.PostResponse;
import src.services.PostService;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostsController {
    PostService postService;
    PostProperties postProps;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request, @AuthenticationPrincipal UserDetails userDetails){
        PostResponse response = postService.createPost(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(@RequestParam(required = false) String title,
                                                          @RequestParam(required = false) String tag,
                                                          Pageable page
                                                          ){
        return ResponseEntity.ok(postService.getFilteredPosts(title, tag, page));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@auth.isPostOwner(#id, authentication.getName) or hasRole('ADMIN')")
    public ResponseEntity<PostResponse> patchPost(@PathVariable("id") Long id, @RequestBody PostRequest request){
        PostResponse response = postService.patchPost(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@auth.isPostOwner(#id, authentication.getName) or hasRole('ADMIN')")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id){
        postService.deletePost(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }


}
