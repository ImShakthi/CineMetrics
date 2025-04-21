package com.skthvl.cinemetrics.exception;

public class InvalidApiKeyException extends RuntimeException {
  public InvalidApiKeyException() {
    super("Invalid API key!");
  }

  public InvalidApiKeyException(String message) {
    super(message);
  }

  public InvalidApiKeyException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidApiKeyException(Throwable cause) {
    super(cause);
  }
}
