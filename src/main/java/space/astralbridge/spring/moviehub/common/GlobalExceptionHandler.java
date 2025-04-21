package space.astralbridge.spring.moviehub.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMsg = error.getDefaultMessage();
            errors.put(fieldName, errorMsg);
        });

        return new Result<>(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return new Result<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return new Result<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNoResourceFoundException(NoResourceFoundException e) {
        return new Result<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return new Result<>(400, "Bad Request: " + e.getMessage(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return new Result<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Result<String> handleNoSuchElementException(NoSuchElementException e) {
        return new Result<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public Result<Void> handleHttpMediaTypeNotSupportedException(HttpMediaTypeException e) {
        return new Result<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("Internal Server Error: [{}]{}", e.getClass(), e.getMessage());

        return new Result<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
