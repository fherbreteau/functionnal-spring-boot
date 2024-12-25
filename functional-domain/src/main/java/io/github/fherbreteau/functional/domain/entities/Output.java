package io.github.fherbreteau.functional.domain.entities;

import java.util.List;

public final class Output<T> {

    private final T value;

    private final Failure failure;

    private Output(T value, Failure failure) {
        this.value = value;
        this.failure = failure;
    }

    public static <T> Output<T> success(T value) {
        return new Output<>(value, null);
    }

    public static <T> Output<T> failure(Throwable throwable) {
        return failure(throwable.getMessage());
    }

    public static <T> Output<T> failure(String message) {
        return failure(message, List.of());
    }

    public static <T> Output<T> failure(String message, List<String> reasons) {
        return new Output<>(null, Failure.failure(message, reasons));
    }

    public T getValue() {
        return value;
    }

    public Failure getFailure() {
        return failure;
    }

    public boolean isSuccess() {
        return failure == null;
    }

    public boolean isFailure() {
        return failure != null;
    }

    @Override
    public String toString() {
        String result = "Output{";
        if (value != null) {
            result += "value=" + value;
        } else {
            result += "failure=" + failure;
        }
        return result + '}';
    }
}
