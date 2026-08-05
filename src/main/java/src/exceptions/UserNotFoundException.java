package src.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String username){
        super("Couldn't find user " + username);
    }
}

