package io.github.fherbreteau.functional.exception;

import io.github.fherbreteau.functional.domain.entities.Error;

import java.util.List;

public class PathException extends RuntimeException {
    private final List<String> reasons;

    public PathException(Error error) {
        super(error.getMessage());
        reasons = error.getReasons();
    }

    public List<String> getReasons() {
        return reasons;
    }
}
