package ee.valitit.project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserErrorException extends Exception {

    private HttpStatus HttpStatus;

    public UserErrorException(String message, org.springframework.http.HttpStatus httpStatus) {
        super(message);
        HttpStatus = httpStatus;
    }

    public UserErrorException(String message, Throwable cause, org.springframework.http.HttpStatus httpStatus) {
        super(message, cause);
        HttpStatus = httpStatus;
    }

    public UserErrorException(String message) {
        super(message);
    }

    public UserErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserErrorException(Throwable cause) {
        super(cause);
    }

}
