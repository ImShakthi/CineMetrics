package com.skthvl.cinemetrics.exception;

public class ApiClientException extends RuntimeException {
  public ApiClientException() {
    super("API Client Error!");
  }

  public ApiClientException(String message) {
    super(message);
  }

  public ApiClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApiClientException(Throwable cause) {
    super(cause);
  }
}
