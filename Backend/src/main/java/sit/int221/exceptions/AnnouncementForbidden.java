package sit.int221.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AnnouncementForbidden extends RuntimeException{
    public AnnouncementForbidden(String message) {
        super(message);
    }
}
