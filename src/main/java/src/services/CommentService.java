package src.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import src.exceptions.ResourceNotFoundException;
import src.model.Comment;
import src.model.Post;
import src.model.User;
import src.model.dto.CommentMapper;
import src.model.dto.CommentRequest;
import src.model.dto.CommentResponse;
import src.repositories.CommentRepository;
import src.repositories.PostRepository;
import src.repositories.UserRepository;

@Service
@AllArgsConstructor
public class CommentService {
    CommentRepository commentRepo;
    PostRepository postRepo;
    UserRepository userRepo;
    CommentMapper commentMapper;

    public Page<CommentResponse> getPostComments(Long id, Pageable page){
        return commentRepo.findByPostId(id, page).map(commentMapper::toResponse);
    }

    public CommentResponse postComment(Long id, CommentRequest commentRequest, String username){
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post " + id + " not found"));
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));

        Comment comment = new Comment();
        comment.setCommentText(commentRequest.getCommentText());
        comment.setAuthor(user);
        comment.setPost(post);

        return commentMapper.toResponse(commentRepo.save(comment));
    }
}
