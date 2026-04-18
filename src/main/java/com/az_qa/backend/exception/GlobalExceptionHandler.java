package com.az_qa.backend.exception;

import com.az_qa.backend.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global exception handler for all controllers.
 *
 * <p>Intercepts exceptions thrown anywhere in the application and maps them
 * to structured {@link ErrorResponse} objects with the appropriate HTTP status.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles {@link ResourceNotFoundException}.
   *
   * @param ex the exception containing the not-found message
   * @return 404 with the exception message as the error body
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(404, ex.getMessage(), LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  /**
   * Handles {@link ItemNotFoundException}.
   *
   * @param ex the exception containing the not-found message
   * @return 404 with the exception message as the error body
   */
  @ExceptionHandler(ItemNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleItemNotFound(ItemNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(404, ex.getMessage(), LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  /**
   * Handles {@link MethodArgumentNotValidException}, triggered when {@code @Valid}
   * fails on a {@code @RequestBody} field.
   *
   * <p>Collects all field errors and joins them into a single message,
   * e.g. {@code "email: must not be blank, name: size must be between 2 and 50"}.</p>
   *
   * @param ex the validation exception from Spring
   * @return 400 with a comma-separated list of field violations
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));

    ErrorResponse error = new ErrorResponse(400, message, LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Handles {@link ConstraintViolationException}, triggered when constraint annotations
   * such as {@code @Positive} or {@code @Min} fail on a {@code @PathVariable}
   * or {@code @RequestParam}.
   *
   * <p>Requires {@code @Validated} on the controller class to activate.</p>
   *
   * @param ex the constraint violation exception
   * @return 400 with a comma-separated list of constraint violations
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
    String message =
        ex.getConstraintViolations().stream()
            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
            .collect(Collectors.joining(", "));

    ErrorResponse error = new ErrorResponse(400, message, LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Handles {@link HttpMessageNotReadableException}, triggered when the request body
   * is malformed or contains a wrong type (e.g. {@code "abc"} where a number was expected).
   *
   * @param ex the unreadable message exception
   * @return 400 with a generic malformed body message
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
    ErrorResponse error =
        new ErrorResponse(400, "Invalid or malformed request body", LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Fallback handler for any exception not explicitly handled above.
   *
   * <p>Prevents internal error details from leaking to the client.</p>
   *
   * @param ex the unhandled exception
   * @return 500 with a generic error message
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    System.out.println("EXCEPCION: " + ex.getClass().getName());
    ErrorResponse error =
        new ErrorResponse(500, "An unexpected error occurred", LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  /**
   * Handles {@link HandlerMethodValidationException}, triggered in Spring Boot 3.2+
   * when constraint annotations such as {@code @Positive} fail on
   * a {@code @PathVariable} or {@code @RequestParam}.
   *
   * @param ex the handler method validation exception
   * @return 400 with a comma-separated list of violations
   */
  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<ErrorResponse> handleMethodValidation(HandlerMethodValidationException ex) {

    String message =
        ex.getParameterValidationResults().stream()
            .flatMap(r -> r.getResolvableErrors().stream())
            .map(MessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));

    ErrorResponse error = new ErrorResponse(400, message, LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  /**
   * Handles {@link MethodArgumentTypeMismatchException}, triggered when a
   * {@code @PathVariable} or {@code @RequestParam} receives a value that cannot
   * be converted to the expected type (e.g. "abc" where a Long was expected).
   *
   * @param ex the type mismatch exception
   * @return 400 with a descriptive message indicating the invalid value
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

    String message = "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'";

    ErrorResponse error = new ErrorResponse(400, message, LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
}
