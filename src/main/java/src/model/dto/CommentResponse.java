package src.model.dto;

import lombok.Data;
import src.model.User;

@Data
public class CommentResponse {
    Long id;
    String commentText;
    User author;
}
