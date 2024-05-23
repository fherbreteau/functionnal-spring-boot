package io.github.fherbreteau.functional.domain.entities;

import java.util.List;
import java.util.Objects;

public final class Error {

    private final String message;
    private final List<String> reasons;

    public static Error error(String message) {
        return error(message, List.of());
    }

    public static Error error(String message, List<String> reasons) {
        return new Error(message, reasons);
    }

    private Error(String message, List<String> reasons) {
        this.message = message;
        this.reasons = reasons;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getReasons() {
        return reasons;
    }

    @Override
    public String toString() {
        return "Error{" +
                "message='" + message + '\'' +
                ", reasons=" + reasons +
                '}';
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
