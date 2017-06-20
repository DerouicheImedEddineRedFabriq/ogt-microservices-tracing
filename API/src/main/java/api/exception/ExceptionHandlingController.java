package api.exception;

import api.ApiConfiguration;
import com.github.kristofa.brave.ServerTracer;
import exception.MyError;
import exception.MyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Tracing Exceptions.
 *
 * @author m.mazigh
 */
@RestControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

    /**
     * Return a ResponseEntity.
     *
     * @param exception my exception.
     * @return a ResponseEntity
     */
    @ExceptionHandler(MyException.class)
    public ResponseEntity<MyError> handleMyException(MyException exception) {
        traceException(exception);
        MyError error = new MyError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Return the message of the exception.
     *
     * @param exception other exception.
     * @return the message of the exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllException(Exception exception) {
       traceException(exception);
        return exception.getMessage();
    }

    private void traceException(Exception exception)
    {
        ServerTracer serverTracer = ApiConfiguration.instance().getBrave().serverTracer();

        if (exception.getMessage() != null) {
            serverTracer.submitBinaryAnnotation("error", exception.getMessage());
        } else {
            serverTracer.submitBinaryAnnotation("error", "");
        }
        StackTraceElement[] stackTrace = exception.getStackTrace();
        String stackTraceString = "";
        if (stackTrace != null) {
            for (StackTraceElement stackTraceElement : stackTrace) {
                stackTraceString += stackTraceElement.toString() + " <br>" ;
            }
        }
        serverTracer.submitBinaryAnnotation("stacktrace", stackTraceString);
        serverTracer.submitBinaryAnnotation("class", exception.getClass().toString());
    }
}
