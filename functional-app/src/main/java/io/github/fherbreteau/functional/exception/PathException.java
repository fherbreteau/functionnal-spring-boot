package io.github.fherbreteau.functional.exception;

import java.io.Serial;

import io.github.fherbreteau.functional.domain.entities.Failure;

public class PathException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3549560013977446562L;

    public PathException(Failure failure) {
        super(failure.getMessage());
    }
}
