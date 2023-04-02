package ru.otus.jdbc.exception;

public class MapperException extends RuntimeException {

  public MapperException(Throwable cause) {
    super(cause);
  }

  public MapperException(String message) {
    super(message);
  }

}
