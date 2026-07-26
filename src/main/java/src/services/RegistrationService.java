package src.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.model.User;
import src.model.dto.RegisterRequest;
import src.repositories.UserRepository;

@Service
public class RegistrationService {
    UserRepository userRepo;

    @Autowired
    public RegistrationService(UserRepository userRepository){
        userRepo = userRepository;
    }

    public User registerUser(RegisterRequest request){
        return userRepo.save(request.toUser());
    }
}
