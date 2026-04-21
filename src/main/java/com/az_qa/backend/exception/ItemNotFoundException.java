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
}
