package exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("User not found!");
    }

    public UserNotFoundException(String username) {
        super("User with username: "+username+" not found!");
    }

    public UserNotFoundException(Long id) {
        super("User with id: "+id+" not found!");
    }
}
