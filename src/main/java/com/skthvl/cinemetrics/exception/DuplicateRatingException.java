package com.skthvl.cinemetrics.exception;

/**
 * Exception thrown when a user attempts to submit multiple ratings for the same movie. This runtime
 * exception is used to maintain data integrity by preventing duplicate ratings from the same user
 * for a single movie.
 */
public class DuplicateRatingException extends RuntimeException {
  /**
   * Constructs a DuplicateRatingException with a default error message indicating that the user has
   * already rated the movie.
   */
  public DuplicateRatingException() {
    super("user already rated this movie");
  }

  /**
   * Constructs a DuplicateRatingException with a custom error message.
   *
   * @param message the detailed error message
   */
  public DuplicateRatingException(String message) {
    super(message);
  }

  /**
   * Constructs a DuplicateRatingException with a custom error message and cause.
   *
   * @param message the detailed error message
   * @param cause the underlying cause of the exception
   */
  public DuplicateRatingException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a DuplicateRatingException with an underlying cause.
   *
   * @param cause the underlying cause of the exception
   */
  public DuplicateRatingException(Throwable cause) {
    super(cause);
  }
}
