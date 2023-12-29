package sit.int221.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer id) {
        super("User id " + id + " does not exist!.");
    }

    public UserNotFoundException(String username) {
        super("User " + username + " does not exist!.");
    }
}
