package corbos.bullscows.controllers;


import java.sql.SQLIntegrityConstraintViolationException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@RestController
public class ExcepHandle extends ResponseEntityExceptionHandler {
    
    private static final String CONSTRAINT_MESSAGE = "Your Item could not be saved. Try again and make sure your item is valid!";
    
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public final ResponseEntity<Error> handleSqlException(SQLIntegrityConstraintViolationException ex, WebRequest request) {
        Error er = new Error();
        er.setMessage(CONSTRAINT_MESSAGE);
        return new ResponseEntity<>(er, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
