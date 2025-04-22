package com.skthvl.cinemetrics.exception;

public class DuplicateRatingException extends RuntimeException {
  public DuplicateRatingException() {
    super("user already rated this movie");
  }

  public DuplicateRatingException(String message) {
    super(message);
  }

  public DuplicateRatingException(String message, Throwable cause) {
    super(message, cause);
  }

  public DuplicateRatingException(Throwable cause) {
    super(cause);
  }
}
