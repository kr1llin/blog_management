package src.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String status;
    private Instant createdDate;
    private String authorUsername;
    private Set<String> tags;
}