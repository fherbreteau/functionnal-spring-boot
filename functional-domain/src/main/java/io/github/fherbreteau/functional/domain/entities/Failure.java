package io.github.fherbreteau.functional.domain.entities;

import java.util.List;
import java.util.Objects;

public final class Failure {

    private final String message;
    private final List<String> reasons;

    private Failure(String message, List<String> reasons) {
        this.message = message;
        this.reasons = reasons;
    }

    public static Failure failure(String message) {
        return failure(message, List.of());
    }

    public static Failure failure(String message, List<String> reasons) {
        return new Failure(message, reasons);
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
        if (!(o instanceof Failure failure)) {
            return false;
        }
        return Objects.equals(message, failure.message);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(message);
    }
}
