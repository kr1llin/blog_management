package src.model.dto;

import org.mapstruct.Mapper;
import src.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
