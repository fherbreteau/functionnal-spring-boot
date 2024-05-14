package io.github.fherbreteau.functional.exception;

import io.github.fherbreteau.functional.domain.entities.Error;

import java.io.IOException;

public class CommandException extends RuntimeException {
    public CommandException(Error error) {
        super(error.getMessage());
    }

    public CommandException(IOException exception) {
        super(exception.getMessage());
    }
}
