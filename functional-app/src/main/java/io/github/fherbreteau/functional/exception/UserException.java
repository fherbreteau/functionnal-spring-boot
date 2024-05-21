package io.github.fherbreteau.functional.exception;

import io.github.fherbreteau.functional.domain.entities.Error;

import java.util.List;

public class UserException extends RuntimeException {
    private final List<String> reasons;

    public UserException(Error error) {
        super(error.getMessage());
        reasons = error.getReasons();
    }

    public List<String> getReasons() {
        return reasons;
    }
}
