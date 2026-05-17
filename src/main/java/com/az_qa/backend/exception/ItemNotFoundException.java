/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.exception;

/**
 * Exception thrown when a requested item cannot be found in the data source.
 */
public class ItemNotFoundException extends RuntimeException {

  public ItemNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ItemNotFoundException(String message) {
    this(message, null);
  }

  public ItemNotFoundException() {
    this("Item not found");
  }

  public DuplicatedItemException withMessage(String message) {
    return new DuplicatedItemException(message, this);
  }
}
