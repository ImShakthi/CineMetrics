package com.skthvl.cinemetrics.exception;

public class MovieNotFoundException extends RuntimeException {
  public MovieNotFoundException() {
    super("Movie not found");
  }

  public MovieNotFoundException(String message) {
    super(message);
  }

  public MovieNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public MovieNotFoundException(Throwable cause) {
    super(cause);
  }
}
