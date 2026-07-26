package src.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(access= AccessLevel.PRIVATE, force = true)
@Table(name="posts")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @CreationTimestamp
    Instant createdAt;

    String title;
    String body;

    @OneToMany
    List<Comment> comments;

    @ManyToOne
    User author;
}
