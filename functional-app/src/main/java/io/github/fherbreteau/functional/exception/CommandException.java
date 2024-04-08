package io.github.fherbreteau.functional.exception;

import io.github.fherbreteau.functional.domain.entities.Error;

public class CommandException extends RuntimeException {
    public CommandException(Error error) {
        super(error.getMessage());
    }
}
