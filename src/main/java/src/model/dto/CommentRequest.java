package src.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank String commentText;
}
