package io.github.fherbreteau.functional.exception;

import java.io.Serial;

import io.github.fherbreteau.functional.domain.entities.Failure;

public class UserException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1704075955258498228L;

    public UserException(Failure failure) {
        super(failure.getMessage());
    }
}
