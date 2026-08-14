package src.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import src.model.dto.CommentRequest;
import src.model.dto.CommentResponse;
import src.services.CommentService;

@RestController
@RequestMapping("/posts/{id}/comments")
@AllArgsConstructor
public class CommentsController {
    CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<CommentResponse>> getPostComments(@PathVariable Long postId,
                                                                 @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable page){
        return ResponseEntity.ok(commentService.getPostComments(postId, page));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommentResponse> postComment(@PathVariable Long postId,
                                                       @Valid CommentRequest comment,
                                                       Authentication authentication){
        String username = authentication.getName();
        CommentResponse commentCreated = commentService.postComment(postId, comment, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentCreated);
    }
}
