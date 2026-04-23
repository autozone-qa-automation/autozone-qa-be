/*
Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software
TC3005B GPO500 - 2026
Autozone QA Automation
*/

package com.az_qa.backend.exception;

public class ItemNotFoundException extends RuntimeException {

  public ItemNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
    // TODO: Log the exception
  }

  public ItemNotFoundException(final String message) {
    this(message, null);
  }

  public ItemNotFoundException() {
    this("Item not found");
  }
}
