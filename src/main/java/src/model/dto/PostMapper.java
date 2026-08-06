package src.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import src.model.Post;
import src.model.Tag;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "authorUsername",  expression = "java(post.getAuthor().getUsername())")
    @Mapping(target = "status", expression = "java(post.getStatus().name()")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagsToStrings")
    PostResponse toResponse(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Post toEntity(PostRequest request);

    @Named("tagsToStrings")
    default Set<String> tagsToStrings(Set<Tag> tags){
        if (tags == null) return new HashSet<>();
        return tags.stream().map(Tag::getName).collect(Collectors.toSet());
    }
}
