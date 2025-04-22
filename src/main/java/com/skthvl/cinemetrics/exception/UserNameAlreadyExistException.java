package com.skthvl.cinemetrics.exception;

public class UserNameAlreadyExistException extends RuntimeException {
  public UserNameAlreadyExistException() {
    super("User name already exist!");
  }

  public UserNameAlreadyExistException(String message) {
    super(message);
  }

  public UserNameAlreadyExistException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserNameAlreadyExistException(Throwable cause) {
    super(cause);
  }
}
