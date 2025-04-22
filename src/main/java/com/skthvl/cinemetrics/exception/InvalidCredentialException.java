package com.skthvl.cinemetrics.exception;

public class InvalidCredentialException extends RuntimeException {
  public InvalidCredentialException() {
    super("user credential is invalid!");
  }

  public InvalidCredentialException(String message) {
    super(message);
  }

  public InvalidCredentialException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidCredentialException(Throwable cause) {
    super(cause);
  }
}
