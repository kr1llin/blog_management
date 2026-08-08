package src.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import src.model.User;
import src.model.dto.RegisterRequest;
import src.repositories.UserRepository;

@Service

public class RegistrationService {
    final UserRepository userRepo;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        userRepo = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest request){
        User user = request.toUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepo.save(user);
    }
}
