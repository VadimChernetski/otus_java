package ru.otus.appcontainer.exception;

public class NoSuchComponentException extends RuntimeException {

  public NoSuchComponentException(String message) {
    super(message);
  }

}
