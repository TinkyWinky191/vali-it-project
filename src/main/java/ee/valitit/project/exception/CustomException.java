package ee.valitit.project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends Exception {

    private HttpStatus HttpStatus;

    public CustomException(String message, org.springframework.http.HttpStatus httpStatus) {
        super(message);
        HttpStatus = httpStatus;
    }

    public CustomException(String message, Throwable cause, org.springframework.http.HttpStatus httpStatus) {
        super(message, cause);
        HttpStatus = httpStatus;
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

}
