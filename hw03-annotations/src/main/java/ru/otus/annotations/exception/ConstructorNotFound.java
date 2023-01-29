package ru.otus.annotations.exception;

public class ConstructorNotFound extends RuntimeException {

    public ConstructorNotFound(String message) {
        super(message);
    }

}
