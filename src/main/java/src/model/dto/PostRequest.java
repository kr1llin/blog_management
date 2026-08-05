package src.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PostRequest {
    @NotBlank(message = "Title is mandatory!")
    private String title;

    @NotBlank(message = "Content is mandatory")
    private String content;

    private Set<String> tagNames;
}
