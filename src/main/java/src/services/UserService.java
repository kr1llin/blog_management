package src.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import src.model.User;
import src.model.dto.UserMapper;
import src.model.dto.UserResponse;
import src.repositories.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepo;
    UserMapper userMapper;

    public Page<UserResponse> getAll(Pageable page){
        Page<User> users = userRepo.findAll(page);
        return users.map(userMapper::toResponse);
    }

    public void deleteById(Long id){
        userRepo.deleteById(id);
    }
}
