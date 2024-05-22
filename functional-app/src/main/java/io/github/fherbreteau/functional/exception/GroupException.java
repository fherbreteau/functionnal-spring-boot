package io.github.fherbreteau.functional.exception;

import io.github.fherbreteau.functional.domain.entities.Error;

public class GroupException extends RuntimeException {
    public GroupException(Error error) {
        super(error.getMessage());
    }
}
