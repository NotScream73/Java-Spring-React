package ip.labwork.user.service;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String login) {
        super(String.format("User not found '%s'", login));
    }
}
