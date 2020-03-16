package ee.valitit.project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends Exception {

    private HttpStatus HttpStatus;

    public UserException(String message, org.springframework.http.HttpStatus httpStatus) {
        super(message);
        HttpStatus = httpStatus;
    }

    public UserException(String message, Throwable cause, org.springframework.http.HttpStatus httpStatus) {
        super(message, cause);
        HttpStatus = httpStatus;
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

}
