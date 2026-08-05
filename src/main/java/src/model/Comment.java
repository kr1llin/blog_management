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
@NoArgsConstructor(access= AccessLevel.PRIVATE, force = true)
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
}
