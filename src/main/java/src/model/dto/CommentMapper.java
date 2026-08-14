package src.model.dto;

import org.mapstruct.Mapper;
import src.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    public CommentResponse toResponse(Comment comment);
}
