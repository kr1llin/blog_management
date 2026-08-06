package src.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resourceName){
        super("Couldn't find resource " + resourceName);
    }
}

