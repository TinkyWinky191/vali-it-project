package ee.valitit.project.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionsResponse> handleException(CustomException exc) {
        ExceptionsResponse response = new ExceptionsResponse();
        response.setMessage(exc.getMessage());
        response.setStatus(exc.getHttpStatus().value());
        response.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, exc.getHttpStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ExceptionsResponse> handleConstraintViolation(ConstraintViolationException exc) {
        ExceptionsResponse response = new ExceptionsResponse();
        String messages = exc.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        response.setMessage(messages);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({TransactionSystemException.class})
    public ResponseEntity<Object> handleException(TransactionSystemException exc) {
        Throwable cause = exc.getCause().getCause();
        if (cause instanceof ConstraintViolationException) {
            ExceptionsResponse response = new ExceptionsResponse();
            String messages = ((ConstraintViolationException) cause).getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            response.setMessage(messages);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setTimeStamp(System.currentTimeMillis());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ExceptionHandler({JsonMappingException.class})
    public ResponseEntity<?> handleJsonMappingException(Exception ex) {
        System.out.println("WTF");
        return new ResponseEntity<>(
                new ExceptionsResponse( HttpStatus.BAD_REQUEST.value(),
                                "Wrong Json received!",
                                        System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
    }

}
