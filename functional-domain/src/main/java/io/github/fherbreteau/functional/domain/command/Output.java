package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.entities.Error;

public class Output {

    private final Object value;

    private final Error error;

    public Output(Object value) {
        this(value, null);
    }

    public Output(Error error) {
        this(null, error);
    }

    private Output(Object value, Error error) {
        this.value = value;
        this.error = error;
    }

    public Object getValue() {
        return value;
    }

    public Error getError() {
        return error;
    }

    public boolean isSuccess() {
        return value != null;
    }

    public boolean isError() {
        return error != null;
    }
}
