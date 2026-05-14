package com.az_qa.backend.exception;

public class ItemIdMismatchException extends RuntimeException {

  public ItemIdMismatchException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ItemIdMismatchException(final String message) {
    super(message);
  }

  public ItemIdMismatchException() {
    super("Item ID mismatch");
  }
}
