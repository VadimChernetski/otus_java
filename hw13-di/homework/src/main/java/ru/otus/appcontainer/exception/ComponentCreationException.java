package ru.otus.appcontainer.exception;

public class ComponentCreationException extends RuntimeException {

  public ComponentCreationException(String message) {
    super(message);
  }

  public ComponentCreationException(Throwable cause) {
    super(cause);
  }
}
