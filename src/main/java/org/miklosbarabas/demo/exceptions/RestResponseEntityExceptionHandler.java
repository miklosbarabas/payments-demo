package org.miklosbarabas.demo.exceptions;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * REST response exception handlers
 *
 * @author Miklos Barabas
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleRepositoryConstraintViolationException(DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex instanceof DuplicateKeyException) {
            errors.put("error", "entity with this id already exists");
        }
        else {
            errors.put("error", "data integrity violation");
        }
        return new ResponseEntity<>(errors, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        PropertyNamingStrategy.SnakeCaseStrategy snakeCaseStrategy = new PropertyNamingStrategy.SnakeCaseStrategy();
        for (ConstraintViolation<?> constraintViolation: constraintViolations) {
            errors.put(snakeCaseStrategy.translate(constraintViolation.getPropertyPath().toString()), constraintViolation.getMessage());
        }

        response.put("error", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}