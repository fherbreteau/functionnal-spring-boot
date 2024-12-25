package io.github.fherbreteau.functional.exception;

import java.io.Serial;

import io.github.fherbreteau.functional.domain.entities.Failure;

public class GroupException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7887371377428360121L;

    public GroupException(Failure failure) {
        super(failure.getMessage());
    }
}
