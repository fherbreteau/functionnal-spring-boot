package io.github.fherbreteau.functional.exception;

import io.github.fherbreteau.functional.domain.entities.Error;

import java.io.IOException;
import java.util.List;

public class CommandException extends RuntimeException {
    private final List<String> reasons;

    public CommandException(Error error) {
        super(error.getMessage());
        reasons = error.getReasons();
    }

    public CommandException(IOException exception) {
        super(exception.getMessage());
        reasons = List.of();
    }

    public List<String> getReasons() {
        return reasons;
    }
}
