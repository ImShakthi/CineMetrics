package com.skthvl.cinemetrics.exception;

import com.skthvl.cinemetrics.model.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application that provides centralized exception handling across
 * all {@code @RequestMapping} methods through {@code @ExceptionHandler} methods. This class
 * translates various exceptions into appropriate HTTP responses with error messages.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles {@link IllegalArgumentException} by returning a BAD_REQUEST response with the exception
   * message.
   *
   * @param ex the IllegalArgumentException that was thrown
   * @return ResponseEntity containing the error message with HTTP status 400 (BAD_REQUEST)
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(final IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
  }

  /**
   * Handles validation errors that occur during request processing. Maps field-specific validation
   * errors to their corresponding error messages.
   *
   * @param ex the MethodArgumentNotValidException containing validation errors
   * @return ResponseEntity containing a map of field names to error messages with HTTP status 400
   *     (BAD_REQUEST)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
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
 
  /**
   * Handles constraint violation exceptions that occur during validation. Maps violated constraints
   * to their corresponding error messages.
   *
   * @param ex the ConstraintViolationException containing validation errors
   * @return ResponseEntity containing a map of constraint paths to error messages with HTTP status
   *     400 (BAD_REQUEST)
   */
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

  /**
   * Handles various business logic exceptions that should result in a BAD_REQUEST response. This
   * includes cases like movie not found, duplicate username, and duplicate ratings.
   *
   * @param ex the Exception that was thrown
   * @return ResponseEntity containing the error message with HTTP status 400 (BAD_REQUEST)
   */
  @ExceptionHandler({
    MovieNotFoundException.class,
    UserNameAlreadyExistException.class,
    DuplicateRatingException.class,
    ValidationException.class
  })
  public ResponseEntity<ErrorResponse> handleBadRequest(final Exception ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
  }

  /**
   * Handles cases where a requested user does not exist in the system.
   *
   * @param ex the UserDoesNotExistException that was thrown
   * @return ResponseEntity containing the error message with HTTP status 404 (NOT_FOUND)
   */
  @ExceptionHandler({UserDoesNotExistException.class})
  public ResponseEntity<ErrorResponse> handleUserDoesNotExistException(final Exception ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
  }

  /**
   * Handles authentication-related exceptions such as invalid API keys or credentials.
   *
   * @param ex the Exception that was thrown (either InvalidApiKeyException or
   *     InvalidCredentialException)
   * @return ResponseEntity containing the error message with HTTP status 401 (UNAUTHORIZED)
   */
  @ExceptionHandler({InvalidApiKeyException.class, InvalidCredentialException.class})
  public ResponseEntity<ErrorResponse> handleInvalidApiKeyException(final Exception ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
  }

  /**
   * Fallback handler for all unhandled exceptions. Logs the exception and returns a generic error
   * message to avoid exposing sensitive information.
   *
   * @param ex the Exception that was thrown
   * @return ResponseEntity containing a generic error message with HTTP status 500
   *     (INTERNAL_SERVER_ERROR)
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(final Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("something went wrong, please try again later"));
  }
}
