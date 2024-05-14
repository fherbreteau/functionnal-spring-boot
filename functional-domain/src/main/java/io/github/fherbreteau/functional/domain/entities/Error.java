package io.github.fherbreteau.functional.domain.entities;

import java.util.Objects;

public class Error {

    private final String message;

    public Error(CommandType type, Input input, User actor) {
        this(String.format("%s with arguments %s failed for %s", type, input, actor));
    }

    public Error(Item item, String segment, User actor) {
        this(String.format("%s not found in %s for %s", segment, item, actor));
    }

    public Error(String message) {
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
