package src.model.dto;

import lombok.Data;
import src.model.User;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String role;

    public User toUser(){
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        return newUser;
    }
}
