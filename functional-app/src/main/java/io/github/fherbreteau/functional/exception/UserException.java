package io.github.fherbreteau.functional.exception;

import io.github.fherbreteau.functional.domain.entities.Error;

public class UserException extends RuntimeException {
    public UserException(Error error) {
        super(error.getMessage());
    }
}
