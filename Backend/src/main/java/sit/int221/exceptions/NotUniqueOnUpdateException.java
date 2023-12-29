package sit.int221.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotUniqueOnUpdateException extends RuntimeException{
    List<String> fieldError;

    public NotUniqueOnUpdateException(List<String> fieldError) {
        super("Bad Request");
        this.fieldError = fieldError;
    }
}