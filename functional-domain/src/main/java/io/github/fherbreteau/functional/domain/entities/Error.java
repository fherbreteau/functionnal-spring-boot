package io.github.fherbreteau.functional.domain.entities;

import java.util.Objects;

public final class Error {

    private final String message;

    public static Error error(String message) {
        return new Error(message);
    }

    private Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Error{message='" + message + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Error error)) {
            return false;
        }
        return Objects.equals(message, error.message);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(message);
    }
}
