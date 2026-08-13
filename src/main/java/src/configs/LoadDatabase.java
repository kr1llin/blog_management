package src.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import src.model.User;
import src.repositories.UserRepository;

@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepo){
        return args -> {
            log.info("Preloading " + userRepo.save(new User("bob", "bob")));
        };
    }
}