package io.github.fherbreteau.functional.exception;

import io.github.fherbreteau.functional.domain.entities.Error;

public class PathException extends RuntimeException {
    public PathException(Error error) {
        super(error.getMessage());
    }
}
