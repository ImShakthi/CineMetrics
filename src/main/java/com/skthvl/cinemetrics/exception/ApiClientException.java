package com.skthvl.cinemetrics.exception;


/**
 * Custom runtime exception thrown when API client operations fail.
 * This exception is used to wrap and handle errors that occur during API client interactions.
 */
public class ApiClientException extends RuntimeException {
  /**
   * Constructs a new ApiClientException with a default error message.
   */
  public ApiClientException() {
    super("API Client Error!");
  }

  /**
   * Constructs a new ApiClientException with the specified error message.
   *
   * @param message the detail message explaining the error condition
   */
  public ApiClientException(String message) {
    super(message);
  }

  /**
   * Constructs a new ApiClientException with the specified error message and cause.
   *
   * @param message the detail message explaining the error condition
   * @param cause the underlying cause of this exception
   */
  public ApiClientException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new ApiClientException with the specified cause.
   *
   * @param cause the underlying cause of this exception
   */
  public ApiClientException(Throwable cause) {
    super(cause);
  }
}
