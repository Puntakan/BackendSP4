package sit.int221.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AnnouncementNotFoundException extends RuntimeException {
    public AnnouncementNotFoundException(Integer id) {
        super("Announcement id " + id + " does not exist!.");
    }
}
