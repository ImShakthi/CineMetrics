package com.skthvl.cinemetrics.exception;

public class UserDoesNotExistException extends RuntimeException {
  public UserDoesNotExistException() {
    super("User doesn't exist!");
  }

  public UserDoesNotExistException(String message) {
    super(message);
  }

  public UserDoesNotExistException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserDoesNotExistException(Throwable cause) {
    super(cause);
  }
}
