package io.github.fherbreteau.functional.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String name) {
        super(name + " not found");
    }

    public NotFoundException(String name, Throwable cause) {
        super(name + " not found", cause);
    }
}
