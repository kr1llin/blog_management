package src.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotBlank
    String commentText;

    @CreationTimestamp
    Instant postedAt;

    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Post post;
}
