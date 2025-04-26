package com.skthvl.cinemetrics.exception;

/**
 * Exception thrown when an invalid API key is provided during API authentication.
 * This runtime exception indicates that the client has provided an API key that is either
 * missing, expired, or not recognized by the system.
 */

public class InvalidApiKeyException extends RuntimeException {
  /**
   * Constructs a new InvalidApiKeyException with a default error message.
   */
  public InvalidApiKeyException() {
    super("Invalid API key!");
  }

  /**
   * Constructs a new InvalidApiKeyException with the specified error message.
   *
   * @param message the detailed error message
   */
  public InvalidApiKeyException(String message) {
    super(message);
  }

  /**
   * Constructs a new InvalidApiKeyException with the specified error message and cause.
   *
   * @param message the detailed error message
   * @param cause the cause of this exception
   */
  public InvalidApiKeyException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new InvalidApiKeyException with the specified cause.
   *
   * @param cause the cause of this exception
   */
  public InvalidApiKeyException(Throwable cause) {
    super(cause);
  }
}
