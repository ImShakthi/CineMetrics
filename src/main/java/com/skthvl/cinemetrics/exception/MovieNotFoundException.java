package com.skthvl.cinemetrics.exception;

/**
 * Exception thrown when a requested movie cannot be found in the system. This is a runtime
 * exception that typically indicates a data access or validation error when attempting to retrieve
 * or process movie-related operations.
 */
public class MovieNotFoundException extends RuntimeException {
  /** Constructs a new MovieNotFoundException with a default message. */
  public MovieNotFoundException() {
    super("Movie not found");
  }

  /**
   * Constructs a new MovieNotFoundException with a specified error message.
   *
   * @param message the detail message describing the error
   */
  public MovieNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructs a new MovieNotFoundException with a specified error message and cause.
   *
   * @param message the detail message describing the error
   * @param cause the cause of the exception
   */
  public MovieNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new MovieNotFoundException with a specified cause.
   *
   * @param cause the cause of the exception
   */
  public MovieNotFoundException(Throwable cause) {
    super(cause);
  }
}
