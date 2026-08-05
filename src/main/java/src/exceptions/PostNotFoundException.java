package src.exceptions;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(Long id){
        super("Couldn't find post " + id);
    }
}
