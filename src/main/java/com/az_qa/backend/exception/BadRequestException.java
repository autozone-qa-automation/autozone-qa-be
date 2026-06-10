/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.exception;

/**
 * Exception thrown when an attempt is made to create or insert an item that
 * already exists in the data source.
 */
public class BadRequestException extends RuntimeException {

  /**
   * Constructs a new {@code BadRequestException} with the specified detail
   * message and cause.
   *
   * @param message the detail message describing the bad request
   * @param cause   the underlying cause of the exception, or {@code null} if none
   */
  public BadRequestException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new {@code BadRequestException} with the specified detail
   * message.
   *
   * @param message the detail message describing the bad request
   */
  public BadRequestException(final String message) {
    this(message, null);
  }

  /**
   * Constructs a new {@code BadRequestException} with the default message
   * {@code "Item already exists"}.
   */
  public BadRequestException() {
    this("Invalid request");
  }
}
