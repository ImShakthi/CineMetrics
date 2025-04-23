package com.skthvl.cinemetrics.exception;

import com.skthvl.cinemetrics.model.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(final IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(
      MethodArgumentNotValidException ex) {
    final Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> {
              errors.put(error.getField(), error.getDefaultMessage());
            });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    final Map<String, String> errors = new HashMap<>();

    ex.getConstraintViolations()
        .forEach(
            violation -> {
              String field = violation.getPropertyPath().toString();
              String message = violation.getMessage();
              errors.put(field, message);
            });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler({
    MovieNotFoundException.class,
    UserDoesNotExistException.class,
    UserNameAlreadyExistException.class,
    DuplicateRatingException.class
  })
  public ResponseEntity<ErrorResponse> handleMovieNotFoundException(final Exception ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler({InvalidApiKeyException.class, InvalidCredentialException.class})
  public ResponseEntity<ErrorResponse> handleInvalidApiKeyException(final Exception ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(final Exception ex) {
    ex.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("something went wrong, please try again later"));
  }
}
