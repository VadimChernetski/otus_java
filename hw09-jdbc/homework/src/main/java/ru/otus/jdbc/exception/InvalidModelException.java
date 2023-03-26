package ru.otus.jdbc.exception;

public class InvalidModelException extends RuntimeException {

  public InvalidModelException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidModelException(String message) {
    super(message);
  }

  public InvalidModelException(Throwable cause) {
    super(cause);
  }
}
